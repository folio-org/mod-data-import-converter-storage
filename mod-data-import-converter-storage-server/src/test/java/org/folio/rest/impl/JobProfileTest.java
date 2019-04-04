package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.folio.rest.jaxrs.model.JobProfile.DataType.DELIMITED;
import static org.folio.rest.jaxrs.model.JobProfile.DataType.MARC;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class JobProfileTest extends AbstractRestVerticleTest {

  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String JOB_PROFILES_PATH = "/data-import-profiles/jobProfiles";

  private static JobProfile jobProfile_1 = new JobProfile().withName("Bla")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
    .withDataType(MARC);
  private static JobProfile jobProfile_2 = new JobProfile().withName("Boo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
    .withDataType(MARC);
  private static JobProfile jobProfile_3 = new JobProfile().withName("Foo")
    .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
    .withDataType(MARC);

  @Test
  public void shouldReturnEmptyListOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("jobProfiles", empty());
  }

  @Test
  public void shouldReturnAllProfilesOnGet() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnCommittedProfilesOnGetWithQueryByLastName() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "?query=userInfo.lastName=Doe")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("jobProfiles*.userInfo.lastName", everyItem(is("Doe")));
  }

  @Test
  public void shouldReturnIpsumTaggedProfilesOnGetWithQueryByTag() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "?query=tags.tagList=/respectCase/respectAccents \\\"ipsum\\\"")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("jobProfiles.get(0).tags.tagList", hasItem("ipsum"))
      .body("jobProfiles.get(1).tags.tagList", hasItem("ipsum"));
  }

  @Test
  public void shouldReturnLimitedCollectionOnGetWithLimit() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "?limit=2")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("jobProfiles.size()", is(2))
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnBadRequestOnPost() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(jobProfile_1)
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .body("name", is(jobProfile_1.getName()))
      .body("tags.tagList", is(jobProfile_1.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"))
      .body("dataType", is(jobProfile_1.getDataType().value()));

    RestAssured.given().spec(spec)
      .body(jobProfile_1)
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors[0].message", is("jobProfile.duplication.invalid"));

  }

  @Test
  public void shouldReturnBadRequestOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .put(JOB_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnNotFoundOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(jobProfile_2)
      .when()
      .put(JOB_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldUpdateProfileOnPut() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_2)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile jobProfile = createResponse.body().as(JobProfile.class);

    jobProfile.setDescription("test");
    jobProfile.setDataType(DELIMITED);
    RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(jobProfile.getId()))
      .body("name", is(jobProfile.getName()))
      .body("description", is("test"))
      .body("tags.tagList", is(jobProfile.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"))
      .body("dataType", is(jobProfile.getDataType().value()));
  }

  @Test
  public void shouldReturnNotFoundOnGetById() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnProfileOnGetById() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_3)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile jobProfile = createResponse.body().as(JobProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(jobProfile.getId()))
      .body("name", is(jobProfile.getName()))
      .body("tags.tagList", is(jobProfile.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"))
      .body("dataType", is(jobProfile.getDataType().value()));
  }

  @Test
  public void shouldReturnNotFoundOnDelete() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldDeleteProfileOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_2)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile profile = createResponse.body().as(JobProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  public void shouldReturnUnprocessableEntityOnPutJobProfileWithExistingName() {
    RestAssured.given()
      .spec(spec)
      .body(jobProfile_1)
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED));

    JobProfile newJobProfile = new JobProfile()
      .withName("Boo")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")));
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(newJobProfile)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile createdJobProfile = createResponse.body().as(JobProfile.class);

    createdJobProfile.setName(jobProfile_1.getName());
    RestAssured.given()
      .spec(spec)
      .body(createdJobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + createdJobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  private void createProfiles() {
    List<JobProfile> jobProfilesToPost = Arrays.asList(jobProfile_1, jobProfile_2, jobProfile_3);
    for (JobProfile profile : jobProfilesToPost) {
      RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(JOB_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(JOB_PROFILES_TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      }
      async.complete();
    });
  }
}
