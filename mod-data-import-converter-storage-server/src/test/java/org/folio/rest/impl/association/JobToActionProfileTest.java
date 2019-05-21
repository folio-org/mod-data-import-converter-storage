package org.folio.rest.impl.association;

import static org.folio.rest.jaxrs.model.JobProfile.DataType.MARC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.UUID;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.AsyncResult;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.impl.AbstractRestVerticleTest;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(VertxUnitRunner.class)
public class JobToActionProfileTest extends AbstractRestVerticleTest {

  private static final String JOB_TO_ACTION_PROFILES = "job_to_action_profiles";
  private static final String JOB_PROFILES = "job_profiles";
  private static final String ACTION_PROFILES = "action_profiles";
  private static final String JOB_PROFILES_URL = "/data-import-profiles/jobProfiles";
  private static final String ACTION_PROFILES_URL = "/data-import-profiles/actionProfiles";
  private static final String ASSOCIATED_PROFILES_URL = "/data-import-profiles/profileAssociations";
  private static final String DETAILS_BY_MASTER_URL = "/data-import-profiles/profileAssociations/{masterId}/details";
  private static final String MASTERS_BY_DETAIL_URL = "/data-import-profiles/profileAssociations/{detailId}/masters";


  @Test
  public void shouldReturnEmptyOkResultOnGetAll(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_OK);
    async.complete();
  }

  @Test
  public void shouldReturnNotFoundOnGetById(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void shouldPostAndGetById(TestContext testContext) {
    JobProfile jobProfile = createJobProfile(testContext);
    ActionProfile actionProfile = createActionProfile(testContext, "testActionProfile_1");

    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile.getId())
      .withOrder(5)
      .withTriggered(true);

    Async async = testContext.async();
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ProfileAssociation savedProfileAssociation = createResponse.body().as(ProfileAssociation.class);
    async.complete();

    async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ASSOCIATED_PROFILES_URL + "/" + savedProfileAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(savedProfileAssociation.getId()))
      .body("masterProfileId", is(jobProfile.getId()))
      .body("detailProfileId", is(actionProfile.getId()))
      .body("order", is(savedProfileAssociation.getOrder()))
      .body("triggered", is(savedProfileAssociation.getTriggered()));
    async.complete();
  }

  @Test
  public void shouldReturnBadRequestOnPost(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .body(new JobProfile())
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    async.complete();
  }

  @Test
  public void shouldReturnNotFoundOnDelete(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void shouldDeleteProfileOnDelete(TestContext testContext) {
    JobProfile jobProfile = createJobProfile(testContext);
    ActionProfile actionProfile = createActionProfile(testContext, "testActionProfile_1");
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile.getId())
      .withOrder(10)
      .withTriggered(false);

    Async async = testContext.async();
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ASSOCIATED_PROFILES_URL + "/" + savedProfileAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
    async.complete();
  }

  @Test
  public void shouldReturnBadRequestOnPut(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .body(new JobProfile())
      .when()
      .put(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    async.complete();
  }

  @Test
  public void shouldReturnNotFoundOnPut(TestContext testContext) {
    Async async = testContext.async();
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withId(UUID.randomUUID().toString())
      .withMasterProfileId(UUID.randomUUID().toString())
      .withDetailProfileId(UUID.randomUUID().toString());
    RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .put(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void shouldUpdateProfileAssociationOnPut(TestContext testContext) {
    JobProfile jobProfile = createJobProfile(testContext);
    ActionProfile actionProfile1 = createActionProfile(testContext, "testActionProfile_1");
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile1.getId())
      .withOrder(7)
      .withTriggered(true);

    Async async = testContext.async();
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    ActionProfile actionProfile2 = createActionProfile(testContext, "testActionProfile_2");
    savedProfileAssociation.setDetailProfileId(actionProfile2.getId());

    async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .body(savedProfileAssociation)
      .when()
      .put(ASSOCIATED_PROFILES_URL + "/" + savedProfileAssociation.getId())
      .then()
      .statusCode(is(HttpStatus.SC_OK))
      .body("id", is(savedProfileAssociation.getId()))
      .body("masterProfileId", is(jobProfile.getId()))
      .body("detailProfileId", is(actionProfile2.getId()))
      .body("order", is(savedProfileAssociation.getOrder()))
      .body("triggered", is(savedProfileAssociation.getTriggered()));
    async.complete();
  }


  @Test
  public void getDetailActionsByMasterProfile_OK(TestContext testContext) {

    JobProfile jobProfile1 = createJobProfile(testContext);
    JobProfile jobProfile2 = createJobProfile(testContext, "testJobProfile2");

    ActionProfile actionProfile1 = createActionProfile(testContext, "testActionProfile_1");
    ActionProfile actionProfile2 = createActionProfile(testContext, "testActionProfile_2");

    ProfileAssociation profileAssociation1 = new ProfileAssociation()
      .withMasterProfileId(jobProfile1.getId())
      .withDetailProfileId(actionProfile1.getId())
      .withOrder(7)
      .withTriggered(true);

    ProfileAssociation profileAssociation2 = new ProfileAssociation()
      .withMasterProfileId(jobProfile2.getId())
      .withDetailProfileId(actionProfile2.getId())
      .withOrder(7)
      .withTriggered(true);

    Async async = testContext.async();
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation1)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    async = testContext.async();
    ProfileAssociation savedProfileAssociation2 = RestAssured.given()
      .spec(spec)
      .body(profileAssociation2)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    RestAssured.given()
      .spec(spec)
      .queryParam("masterType", "JOB_PROFILE")
      .when()
      .get(DETAILS_BY_MASTER_URL, jobProfile1.getId())
      .then().statusCode(is(HttpStatus.SC_OK))
      .body("contentType", is(JOB_PROFILE.value()))
      .body("id", is(jobProfile1.getId()))
      .body("content.id", is(jobProfile1.getId()))
      .body("content.userInfo.firstName", is(jobProfile1.getUserInfo().getFirstName()))
      .body("content.userInfo.lastName", is(jobProfile1.getUserInfo().getLastName()))
      .body("content.userInfo.userName", is(jobProfile1.getUserInfo().getUserName()))
      .body("content.metadata.createdByUserId", is(jobProfile1.getMetadata().getCreatedByUserId()))
      .body("content.metadata.updatedByUserId", is(jobProfile1.getMetadata().getUpdatedByUserId()))
      .body("childSnapshotWrappers.size()", is(1))
      .body("childSnapshotWrappers[0].id", is(actionProfile1.getId()))
      .body("childSnapshotWrappers[0].contentType", is(ACTION_PROFILE.value()))
      .body("childSnapshotWrappers[0].content.id", is(actionProfile1.getId()))
      .body("childSnapshotWrappers[0].content.name", is(actionProfile1.getName()))
      .body("childSnapshotWrappers[0].content.userInfo.firstName", is(actionProfile1.getUserInfo().getFirstName()))
      .body("childSnapshotWrappers[0].content.userInfo.lastName", is(actionProfile1.getUserInfo().getLastName()))
      .body("childSnapshotWrappers[0].content.userInfo.userName", is(actionProfile1.getUserInfo().getUserName()));
  }

  @Test
  public void getDetailActionsByMasterProfile_NotFound(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("masterType", "JOB_PROFILE")
      .when()
      .get(DETAILS_BY_MASTER_URL, UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void getDetailActionsByMasterProfile_WrongQueryParameter(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("masterType", "foo")
      .when()
      .get(DETAILS_BY_MASTER_URL, UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST)
      .body(is("The specified type: foo is wrong. It should be " + Arrays.toString(ContentType.values())));
    async.complete();
  }

  @Test
  public void getDetailActionsByMasterProfile_emptyDetailsListWithMasterProfile(TestContext testContext) {
    JobProfile jobProfile = createJobProfile(testContext);
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("masterType", "JOB_PROFILE")
      .when()
      .get(DETAILS_BY_MASTER_URL, jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("contentType", is(JOB_PROFILE.value()))
      .body("id", is(jobProfile.getId()))
      .body("content.id", is(jobProfile.getId()))
      .body("content.userInfo.firstName", is(jobProfile.getUserInfo().getFirstName()))
      .body("content.userInfo.lastName", is(jobProfile.getUserInfo().getLastName()))
      .body("content.userInfo.userName", is(jobProfile.getUserInfo().getUserName()))
      .body("content.metadata.createdByUserId", is(jobProfile.getMetadata().getCreatedByUserId()))
      .body("content.metadata.updatedByUserId", is(jobProfile.getMetadata().getUpdatedByUserId()))
      .body("childSnapshotWrappers.size()", is(0));
    async.complete();
  }

  @Test
  public void getMastersByDetailActionProfile_OK(TestContext testContext) {

    JobProfile jobProfile1 = createJobProfile(testContext);
    JobProfile jobProfile2 = createJobProfile(testContext, "testJobProfile2");

    ActionProfile actionProfile1 = createActionProfile(testContext, "testActionProfile_1");
    ActionProfile actionProfile2 = createActionProfile(testContext, "testActionProfile_2");

    ProfileAssociation profileAssociation1 = new ProfileAssociation()
      .withMasterProfileId(jobProfile1.getId())
      .withDetailProfileId(actionProfile1.getId())
      .withOrder(7)
      .withTriggered(true);

    ProfileAssociation profileAssociation2 = new ProfileAssociation()
      .withMasterProfileId(jobProfile2.getId())
      .withDetailProfileId(actionProfile2.getId())
      .withOrder(7)
      .withTriggered(true);

    Async async = testContext.async();
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation1)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    async = testContext.async();
    ProfileAssociation savedProfileAssociation2 = RestAssured.given()
      .spec(spec)
      .body(profileAssociation2)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);
    async.complete();

    RestAssured.given()
      .spec(spec)
      .queryParam("detailType", "ACTION_PROFILE")
      .when()
      .get(MASTERS_BY_DETAIL_URL, actionProfile1.getId())
      .then().statusCode(is(HttpStatus.SC_OK))
      .body("contentType", is(ACTION_PROFILE.value()))
      .body("id", is(actionProfile1.getId()))
      .body("content.id", is(actionProfile1.getId()))
      .body("content.userInfo.firstName", is(actionProfile1.getUserInfo().getFirstName()))
      .body("content.userInfo.lastName", is(actionProfile1.getUserInfo().getLastName()))
      .body("content.userInfo.userName", is(actionProfile1.getUserInfo().getUserName()))
      .body("content.metadata.createdByUserId", is(actionProfile1.getMetadata().getCreatedByUserId()))
      .body("content.metadata.updatedByUserId", is(actionProfile1.getMetadata().getUpdatedByUserId()))
      .body("childSnapshotWrappers.size()", is(1))
      .body("childSnapshotWrappers[0].id", is(jobProfile1.getId()))
      .body("childSnapshotWrappers[0].contentType", is(JOB_PROFILE.value()))
      .body("childSnapshotWrappers[0].content.id", is(jobProfile1.getId()))
      .body("childSnapshotWrappers[0].content.name", is(jobProfile1.getName()))
      .body("childSnapshotWrappers[0].content.userInfo.firstName", is(jobProfile1.getUserInfo().getFirstName()))
      .body("childSnapshotWrappers[0].content.userInfo.lastName", is(jobProfile1.getUserInfo().getLastName()))
      .body("childSnapshotWrappers[0].content.userInfo.userName", is(jobProfile1.getUserInfo().getUserName()));
  }


  @Test
  public void getMastersByDetailActionProfile_NotFound(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("detailType", "ACTION_PROFILE")
      .when()
      .get(MASTERS_BY_DETAIL_URL, UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void getMastersByDetailActionProfile_WrongQueryParameter(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("detailType", "foo")
      .when()
      .get(MASTERS_BY_DETAIL_URL, UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST)
      .body(is("The specified type: foo is wrong. It should be " + Arrays.toString(ContentType.values())));
    async.complete();
  }

  @Test
  public void getMastersByDetailActionProfile_emptyDetailsListWithMasterProfile(TestContext testContext) {
    ActionProfile actionProfile = createActionProfile(testContext, "testActionProfile_1");
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .queryParam("detailType", "ACTION_PROFILE")
      .when()
      .get(MASTERS_BY_DETAIL_URL, actionProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("contentType", is(ACTION_PROFILE.value()))
      .body("id", is(actionProfile.getId()))
      .body("content.id", is(actionProfile.getId()))
      .body("content.userInfo.firstName", is(actionProfile.getUserInfo().getFirstName()))
      .body("content.userInfo.lastName", is(actionProfile.getUserInfo().getLastName()))
      .body("content.userInfo.userName", is(actionProfile.getUserInfo().getUserName()))
      .body("content.metadata.createdByUserId", is(actionProfile.getMetadata().getCreatedByUserId()))
      .body("content.metadata.updatedByUserId", is(actionProfile.getMetadata().getUpdatedByUserId()))
      .body("childSnapshotWrappers.size()", is(0));
    async.complete();
  }

  private JobProfile createJobProfile(TestContext testContext) {

    return createJobProfile(testContext, "testJobProfile");
  }


  private JobProfile createJobProfile(TestContext testContext, String profileName) {

    Async async = testContext.async();
    JobProfile jobProfile = RestAssured.given()
      .spec(spec)
      .body(new JobProfile().withName(profileName).withDataType(MARC))
      .when()
      .post(JOB_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .and()
      .extract().body().as(JobProfile.class);
    async.complete();
    return jobProfile;
  }

  private ActionProfile createActionProfile(TestContext testContext, String profileName) {
    Async async = testContext.async();
    ActionProfile actionProfile = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile().withName(profileName))
      .when()
      .post(ACTION_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .and()
      .extract().body().as(ActionProfile.class);
    async.complete();
    return actionProfile;
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient pgClient = PostgresClient.getInstance(vertx, TENANT_ID);

    pgClient.delete(JOB_TO_ACTION_PROFILES, new Criterion(), associationsDeleteEvent -> {
      stopIfFailed(context, associationsDeleteEvent);
      pgClient.delete(JOB_PROFILES, new Criterion(), jobProfilesDeleteEvent -> {
        stopIfFailed(context, jobProfilesDeleteEvent);
        pgClient.delete(ACTION_PROFILES, new Criterion(), actionProfilesDeleteEvent -> {
          stopIfFailed(context, actionProfilesDeleteEvent);
          async.complete();
        });
      });
    });
  }

  private void stopIfFailed(TestContext context, AsyncResult<UpdateResult> asyncResult) {
    if (asyncResult.failed()) {
      context.fail(asyncResult.cause());
    }
  }
}
