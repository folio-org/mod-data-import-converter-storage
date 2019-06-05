package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.services.util.EntityTypes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class ActionProfileTest extends AbstractRestVerticleTest {

  static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String ACTION_TO_ACTION_PROFILES_TABLE = "action_to_action_profiles";
  static final String ACTION_PROFILES_PATH = "/data-import-profiles/actionProfiles";
  private static final String ENTITY_TYPES_PATH = " /data-import-profiles/entityTypes";

  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";

  private static ActionProfile actionProfile_1 = new ActionProfile().withName("Bla")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")));
  private static ActionProfile actionProfile_2 = new ActionProfile().withName("Boo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")));
  private static ActionProfile actionProfile_3 = new ActionProfile().withName("Foo")
    .withTags(new Tags().withTagList(Collections.singletonList("lorem")));

  @Test
  public void shouldReturnEmptyListOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("actionProfiles", empty());
  }

  @Test
  public void shouldReturnAllProfilesOnGet() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("actionProfiles*.deleted", everyItem(is(false)));
  }

  @Test
  public void shouldReturnCommittedProfilesOnGetWithQueryByLastName() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "?query=userInfo.lastName=Doe")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("actionProfiles*.deleted", everyItem(is(false)))
      .body("actionProfiles*.userInfo.lastName", everyItem(is("Doe")));
  }

  @Test
  public void shouldReturnIpsumTaggedProfilesOnGetWithQueryByTag() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "?query=tags.tagList=/respectCase/respectAccents \\\"ipsum\\\"")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("actionProfiles*.deleted", everyItem(is(false)))
      .body("actionProfiles.get(0).tags.tagList", hasItem("ipsum"))
      .body("actionProfiles.get(1).tags.tagList", hasItem("ipsum"));
  }

  @Test
  public void shouldReturnLimitedCollectionOnGetWithLimit() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "?limit=2")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("actionProfiles.size()", is(2))
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnBadRequestOnPost() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(actionProfile_1)
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("name", is(actionProfile_1.getName()))
      .body("tags.tagList", is(actionProfile_1.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(actionProfile_1)
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .put(ACTION_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnNotFoundOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .put(ACTION_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnUnprocessableEntityOnPutProfileWithExistingName() {
    createProfiles();

    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile().withName("newProfile"))
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile createdProfile = createResponse.body().as(ActionProfile.class);

    createdProfile.setName(actionProfile_1.getName());
    RestAssured.given()
      .spec(spec)
      .body(createdProfile)
      .when()
      .put(ACTION_PROFILES_PATH + "/" + createdProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldUpdateProfileOnPut() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile actionProfile = createResponse.body().as(ActionProfile.class);

    actionProfile.setDescription("test");
    RestAssured.given()
      .spec(spec)
      .body(actionProfile)
      .when()
      .put(ACTION_PROFILES_PATH + "/" + actionProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(actionProfile.getId()))
      .body("description", is("test"))
      .body("name", is(actionProfile.getName()))
      .body("tags.tagList", is(actionProfile.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));
  }

  @Test
  public void shouldReturnNotFoundOnGetById() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnProfileOnGetById() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_3)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile actionProfile = createResponse.body().as(ActionProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + actionProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(actionProfile.getId()))
      .body("name", is(actionProfile.getName()))
      .body("tags.tagList", is(actionProfile.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));
  }

  @Test
  public void shouldReturnNotFoundOnDelete() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnBadRequestOnDeleteProfileAssociatedWithOtherProfiles() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_1)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile profileToDelete = createResponse.body().as(ActionProfile.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile associatedActionProfile = createResponse.body().as(ActionProfile.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", ACTION_PROFILE.value())
      .body(new ProfileAssociation()
        .withMasterProfileId(profileToDelete.getId())
        .withDetailProfileId(associatedActionProfile.getId())
        .withOrder(1))
      .when()
      .post(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED));

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CONFLICT);
  }

  @Test
  public void shouldMarkProfileAsDeletedOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfile profile = createResponse.body().as(ActionProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("deleted", is(true));
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    ActionProfile profileToDelete = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile()
        .withName("ProfileToDelete"))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .param("showDeleted", true)
      .get(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(4));
  }

  @Test
  public void shouldReturnOnlyUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsNotPassed() {
    createProfiles();
    ActionProfile profileToDelete = RestAssured.given()
      .spec(spec)
      .body(new ActionProfile()
        .withName("ProfileToDelete"))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("actionProfiles*.deleted", everyItem(is(false)));
  }

  @Test
  public void shouldReturnAllEntityTypesOnGet() {
    List<String> entityTypesList = Arrays.stream(EntityTypes.values())
      .map(EntityTypes::getName)
      .collect(Collectors.toList());

    Response getResponse = RestAssured.given()
      .spec(spec)
      .when()
      .get(ENTITY_TYPES_PATH);

    getResponse
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(entityTypesList.size()))
      .body("entityTypes", containsInAnyOrder(entityTypesList.toArray()));
  }

  private void createProfiles() {
    List<ActionProfile> actionProfilesToPost = Arrays.asList(actionProfile_1, actionProfile_2, actionProfile_3);
    for (ActionProfile profile : actionProfilesToPost) {
      RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(ACTION_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(ACTION_TO_ACTION_PROFILES_TABLE, new Criterion(), event ->
      PostgresClient.getInstance(vertx, TENANT_ID).delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), event2 -> {
        if (event2.failed()) {
          context.fail(event2.cause());
        }
        async.complete();
      }));
  }
}
