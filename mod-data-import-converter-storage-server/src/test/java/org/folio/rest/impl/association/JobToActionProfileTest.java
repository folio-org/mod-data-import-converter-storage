package org.folio.rest.impl.association;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.impl.AbstractRestVerticleTest;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class JobToActionProfileTest extends AbstractRestVerticleTest {
  private static final String ASSOCIATION_TABLE_NAME = "job_to_action_profiles";
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String JOB_PROFILES_URL = "/data-import-profiles/jobProfiles";
  private static final String ACTION_PROFILES_URL = "/data-import-profiles/actionProfiles";
  private static final String ASSOCIATED_PROFILES_URL = "/data-import-profiles/profileAssociations";

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
    ActionProfile actionProfile = createActionProfile(testContext);

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
    ActionProfile actionProfile = createActionProfile(testContext);
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
    ActionProfile actionProfile1 = createActionProfile(testContext);
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

    ActionProfile actionProfile2 = createActionProfile(testContext);
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

  private JobProfile createJobProfile(TestContext testContext) {
    Async async = testContext.async();
    JobProfile jobProfile = RestAssured.given()
      .spec(spec)
      .body(new JobProfile().withName("testJobProfile"))
      .when()
      .post(JOB_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .and()
      .extract().body().as(JobProfile.class);
    async.complete();
    return jobProfile;
  }

  private ActionProfile createActionProfile(TestContext testContext) {
    Async async = testContext.async();
    ActionProfile actionProfile = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile().withName("testActionProfile"))
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
    pgClient.delete(ASSOCIATION_TABLE_NAME, new Criterion(), associationsDeleteEvent -> {
      if (associationsDeleteEvent.failed()) {
        context.fail(associationsDeleteEvent.cause());
      }
      pgClient.delete(JOB_PROFILES_TABLE_NAME, new Criterion(), jobProfilesDeleteEvent -> {
        if (jobProfilesDeleteEvent.failed()) {
          context.fail(jobProfilesDeleteEvent.cause());
        }
        pgClient.delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), actionProfilesDeleteEvent -> {
          if (actionProfilesDeleteEvent.failed()) {
            context.fail(actionProfilesDeleteEvent.cause());
          }
          async.complete();
        });
      });
    });
  }
}
