package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
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

import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_PATH;
import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_TABLE_NAME;
import static org.folio.rest.impl.MatchProfileTest.MATCH_PROFILES_PATH;
import static org.folio.rest.impl.MatchProfileTest.MATCH_PROFILES_TABLE_NAME;
import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.JobProfile.DataType.DELIMITED;
import static org.folio.rest.jaxrs.model.JobProfile.DataType.MARC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class JobProfileTest extends AbstractRestVerticleTest {

  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  public static final String JOB_TO_ACTION_PROFILES_TABLE = "job_to_action_profiles";
  public static final String JOB_TO_MATCH_PROFILES_TABLE = "job_to_match_profiles";
  private static final String JOB_PROFILES_PATH = "/data-import-profiles/jobProfiles";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";

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
      .body("totalRecords", is(3))
      .body("jobProfiles*.deleted", everyItem(is(false)));
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
      .body("jobProfiles*.deleted", everyItem(is(false)))
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
      .body("jobProfiles*.deleted", everyItem(is(false)))
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
      .body("jobProfiles*.deleted", everyItem(is(false)))
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnSortedProfilesOnGetWhenSortByIsSpecified(TestContext testContext) {
    createProfiles();
    List<JobProfile> jobProfileList = RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "?query=(cql.allRecords=1) sortBy metadata.createdDate/sort.descending")
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .extract().body().as(JobProfileCollection.class).getJobProfiles();

    Assert.assertTrue(jobProfileList.get(0).getMetadata().getCreatedDate().after(jobProfileList.get(1).getMetadata().getCreatedDate()));
    Assert.assertTrue(jobProfileList.get(1).getMetadata().getCreatedDate().after(jobProfileList.get(2).getMetadata().getCreatedDate()));
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
  public void shouldReturnBadRequestOnPostJobProfileWithoutDataType() {
    JsonObject jobProfileWithoutDataType = new JsonObject()
      .put("name", "Bla");

    RestAssured.given()
      .spec(spec)
      .body(jobProfileWithoutDataType.encode())
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPostJobProfileWithInvalidField() {
    JsonObject jobProfile = new JsonObject()
      .put("name", "Bla")
      .put("dataType", MARC)
      .put("invalidField", "value");

    RestAssured.given()
      .spec(spec)
      .body(jobProfile.encode())
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
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
  public void shouldReturnBadRequestOnPutJobProfileWithInvalidField() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_2)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile jobProfile = createResponse.body().as(JobProfile.class);

    JsonObject jobProfileJson = JsonObject.mapFrom(jobProfile)
      .put("invalidField", "value");

    RestAssured.given()
      .spec(spec)
      .body(jobProfileJson.encode())
      .when()
      .put(JOB_PROFILES_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
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
      .log().all()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldMarkProfileAsDeletedOnDelete() {
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

    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("deleted", is(true));
  }

  @Test
  public void shouldDeleteAssociationsWithDetailProfilesOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_1)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfile profileToDelete = createResponse.body().as(JobProfile.class);

    // creation detail-profiles
    createResponse = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile()
        .withName("testAction")
        .withAction(CREATE)
        .withFolioRecord(MARC_BIBLIOGRAPHIC))
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile associatedActionProfile = createResponse.body().as(ActionProfile.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(new MatchProfile()
        .withName("testMatch")
        .withIncomingRecordType(MatchProfile.IncomingRecordType.MARC)
        .withExistingRecordType(MatchProfile.ExistingRecordType.MARC_BIBLIOGRAPHIC)
        .withIncomingDataValueType(MatchProfile.IncomingDataValueType.VALUE_FROM_INCOMING_RECORD))
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile associatedMatchProfile = createResponse.body().as(MatchProfile.class);

    // creation associations
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(profileToDelete.getId())
      .withOrder(1);

    ProfileAssociation jobToActionAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedActionProfile.getId()),
      JOB_PROFILE, ACTION_PROFILE);
    ProfileAssociation jobToMatchAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedMatchProfile.getId()),
      JOB_PROFILE, MATCH_PROFILE);

    // deleting job profile
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    // receiving deleted associations
    RestAssured.given()
      .spec(spec)
      .queryParam("master", JOB_PROFILE.value())
      .queryParam("detail", ACTION_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + jobToActionAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", JOB_PROFILE.value())
      .queryParam("detail", MATCH_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + jobToMatchAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
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
      .withDataType(MARC)
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

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    JobProfile jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new JobProfile().withName("ProfileToDelete")
        .withDataType(MARC))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .param("showDeleted", true)
      .get(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(4));
  }

  @Test
  public void shouldReturnOnlyUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsNotPassed() {
    createProfiles();
    JobProfile jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new JobProfile().withName("ProfileToDelete")
        .withDataType(MARC))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getId())
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("jobProfiles*.deleted", everyItem(is(false)));
  }

  @Test
  public void shouldCreateProfileOnPostWhenWasDeletedProfileWithSameNameBefore() {
    JobProfile jobProfile = new JobProfile().withName("profileName")
      .withDataType(MARC);

    JobProfile jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED);
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

  private ProfileAssociation postProfileAssociation(ProfileAssociation profileAssociation, ContentType masterType, ContentType detailType) {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .queryParam("master", masterType.value())
      .queryParam("detail", detailType.value())
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    return createResponse.body().as(ProfileAssociation.class);
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient pgClient = PostgresClient.getInstance(vertx, TENANT_ID);
    pgClient.delete(JOB_TO_ACTION_PROFILES_TABLE, new Criterion(), event ->
      pgClient.delete(JOB_TO_MATCH_PROFILES_TABLE, new Criterion(), event2 ->
        pgClient.delete(JOB_PROFILES_TABLE_NAME, new Criterion(), event3 ->
          pgClient.delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), event4 ->
            pgClient.delete(MATCH_PROFILES_TABLE_NAME, new Criterion(), event5 -> {
              if (event5.failed()) {
                context.fail(event5.cause());
              }
              async.complete();
            })))));
  }
}
