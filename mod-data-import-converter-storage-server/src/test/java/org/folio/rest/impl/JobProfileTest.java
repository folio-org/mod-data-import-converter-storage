package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.jaxrs.model.UserInfo;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    .withUserInfo(new UserInfo().withFirstName("Jane").withLastName("Doe").withUserName("@janedoe"));
  private static JobProfile jobProfile_2 = new JobProfile().withName("Boo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
    .withUserInfo(new UserInfo().withFirstName("Jane").withLastName("Doe").withUserName("@janedoe"));
  private static JobProfile jobProfile_3 = new JobProfile().withName("Foo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem")))
    .withUserInfo(new UserInfo().withFirstName("John").withLastName("Smith").withUserName("@johnsmith"));

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
      .get(JOB_PROFILES_PATH + "?query=userInfo.lastName=" + jobProfile_1.getUserInfo().getLastName())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("jobProfiles*.userInfo.lastName", everyItem(is(jobProfile_1.getUserInfo().getLastName())));
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
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("name", is(jobProfile_1.getName()))
      .body("tags.tagList", is(jobProfile_1.getTags().getTagList()))
      .body("userInfo.lastName", is(jobProfile_1.getUserInfo().getLastName()))
      .body("userInfo.firstName", is(jobProfile_1.getUserInfo().getFirstName()))
      .body("userInfo.userName", is(jobProfile_1.getUserInfo().getUserName()));
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

    jobProfile.setUserInfo(new UserInfo().withFirstName("John").withLastName("Johnson").withUserName("@johnjohnson"));
    RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(jobProfile.getId()))
      .body("name", is(jobProfile.getName()))
      .body("tags.tagList", is(jobProfile.getTags().getTagList()))
      .body("userInfo.lastName", is(jobProfile.getUserInfo().getLastName()))
      .body("userInfo.firstName", is(jobProfile.getUserInfo().getFirstName()))
      .body("userInfo.userName", is(jobProfile.getUserInfo().getUserName()));
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
      .body("userInfo.lastName", is(jobProfile.getUserInfo().getLastName()))
      .body("userInfo.firstName", is(jobProfile.getUserInfo().getFirstName()))
      .body("userInfo.userName", is(jobProfile.getUserInfo().getUserName()));
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
    PostgresClient.getInstance(vertx, TENANT_ID).delete(JOB_PROFILES_TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      }
    });
  }
}