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
  private static final String TABLE_NAME = "job_to_action_profiles";
  private static final String JOB_PROFILES_URL = "/data-import-profiles/jobProfiles";
  private static final String ACTION_PROFILES_URL = "/data-import-profiles/actionProfiles";
  private static final String ASSOCIATED_PROFILES_URL = "/data-import-profiles/profileAssociations";

  @Test
  public void shouldSaveAndReturnProfileAssociationOnGetById() {
    JobProfile jobProfile = createJobProfile();
    ActionProfile actionProfile = createActionProfile();

    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile.getId())
      .withOrder(5)
      .withTriggered(true);
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ProfileAssociation savedProfileAssociation = createResponse.body().as(ProfileAssociation.class);

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
  }

  @Test
  public void shouldReturnNotFoundOnGetById() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnNotFoundOnDelete() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ASSOCIATED_PROFILES_URL + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldDeleteProfileOnDelete() {
    JobProfile jobProfile = createJobProfile();
    ActionProfile actionProfile = createActionProfile();
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile.getId())
      .withOrder(10)
      .withTriggered(false);
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ASSOCIATED_PROFILES_URL + "/" + savedProfileAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  public void shouldUpdateProfileAssociation() {
    JobProfile jobProfile = createJobProfile();
    ActionProfile actionProfile1 = createActionProfile();
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile1.getId())
      .withOrder(7)
      .withTriggered(true);
    ProfileAssociation savedProfileAssociation = RestAssured.given()
      .spec(spec)
      .body(profileAssociation)
      .when()
      .post(ASSOCIATED_PROFILES_URL)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED))
      .and()
      .extract().body().as(ProfileAssociation.class);

    ActionProfile actionProfile2 = createActionProfile();
    savedProfileAssociation.setDetailProfileId(actionProfile2.getId());

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
  }

  private JobProfile createJobProfile() {
    return RestAssured.given()
      .spec(spec)
      .body(new JobProfile().withName("testJobProfile" + UUID.randomUUID().toString()))
      .when()
      .post(JOB_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .and()
      .extract().body().as(JobProfile.class);
  }

  private ActionProfile createActionProfile() {
    return RestAssured.given()
      .spec(spec)
      .body(new ActionProfile().withName("testActionProfile"))
      .when()
      .post(ACTION_PROFILES_URL)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .and()
      .extract().body().as(ActionProfile.class);
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      } else {
        async.complete();
      }
    });
  }
}
