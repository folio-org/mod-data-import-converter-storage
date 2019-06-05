package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfile.ExistingRecordType;
import org.folio.rest.jaxrs.model.MatchProfile.IncomingDataValueType;
import org.folio.rest.jaxrs.model.ProfileAssociation;
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

import static org.folio.rest.jaxrs.model.MatchProfile.IncomingRecordType.MARC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class MatchProfileTest extends AbstractRestVerticleTest {

  private static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE = "match_to_match_profiles";
  private static final String MATCH_PROFILES_PATH = "/data-import-profiles/matchProfiles";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";

  private static MatchProfile matchProfile_1 = new MatchProfile().withName("Bla")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
    .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD);
  private static MatchProfile matchProfile_2 = new MatchProfile().withName("Boo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
    .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD);
  private static MatchProfile matchProfile_3 = new MatchProfile().withName("Foo")
    .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
    .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD);

  @Test
  public void shouldReturnEmptyListOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("matchProfiles", empty());
  }

  @Test
  public void shouldReturnAllProfilesOnGet() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("matchProfiles*.deleted", everyItem(is(false)));
  }

  @Test
  public void shouldReturnCommittedProfilesOnGetWithQueryByLastName() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "?query=userInfo.lastName=Doe")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("matchProfiles*.deleted", everyItem(is(false)))
      .body("matchProfiles*.userInfo.lastName", everyItem(is("Doe")));
  }

  @Test
  public void shouldReturnIpsumTaggedProfilesOnGetWithQueryByTag() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "?query=tags.tagList=/respectCase/respectAccents \\\"ipsum\\\"")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("matchProfiles.get(0).tags.tagList", hasItem("ipsum"))
      .body("matchProfiles.get(1).tags.tagList", hasItem("ipsum"));
  }

  @Test
  public void shouldReturnLimitedCollectionOnGetWithLimit() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "?limit=2")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("matchProfiles.size()", is(2))
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnBadRequestOnPost() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .post(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(matchProfile_1)
      .when()
      .post(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("name", is(matchProfile_1.getName()))
      .body("tags.tagList", is(matchProfile_1.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(matchProfile_1)
      .when()
      .post(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .put(MATCH_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnNotFoundOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(matchProfile_2)
      .when()
      .put(MATCH_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnUnprocessableEntityOnPutProfileWithExistingName() {
    createProfiles();

    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(new MatchProfile()
        .withName("newProfile")
        .withIncomingRecordType(MARC)
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
        .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD))
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile createdProfile = createResponse.body().as(MatchProfile.class);

    createdProfile.setName(matchProfile_1.getName());
    RestAssured.given()
      .spec(spec)
      .body(createdProfile)
      .when()
      .put(MATCH_PROFILES_PATH + "/" + createdProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldUpdateProfileOnPut() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_2)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile matchProfile = createResponse.body().as(MatchProfile.class);

    matchProfile.setDescription("test");
    RestAssured.given()
      .spec(spec)
      .body(matchProfile)
      .when()
      .put(MATCH_PROFILES_PATH + "/" + matchProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(matchProfile.getId()))
      .body("name", is(matchProfile.getName()))
      .body("description", is("test"))
      .body("tags.tagList", is(matchProfile.getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));
  }

  @Test
  public void shouldReturnNotFoundOnGetById() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnProfileOnGetById() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_3)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile matchProfile = createResponse.body().as(MatchProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "/" + matchProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(matchProfile.getId()))
      .body("name", is(matchProfile.getName()))
      .body("tags.tagList", is(matchProfile.getTags().getTagList()))
      .body("userInfo.lastName", is(matchProfile.getUserInfo().getLastName()))
      .body("userInfo.firstName", is(matchProfile.getUserInfo().getFirstName()))
      .body("userInfo.userName", is(matchProfile.getUserInfo().getUserName()));
  }

  @Test
  public void shouldReturnNotFoundOnDelete() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnBadRequestOnDeleteProfileAssociatedWithOtherProfiles() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_1)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile profileToDelete = createResponse.body().as(MatchProfile.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_2)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile matchProfile = createResponse.body().as(MatchProfile.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", MATCH_PROFILE.value())
      .queryParam("detail", MATCH_PROFILE.value())
      .body(new ProfileAssociation()
        .withMasterProfileId(matchProfile.getId())
        .withDetailProfileId(profileToDelete.getId())
        .withOrder(1))
      .when()
      .post(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED));

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CONFLICT);
  }

  @Test
  public void shouldMarkAsDeletedProfileOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_2)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile profile = createResponse.body().as(MatchProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "/" + profile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("deleted", is(true));
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    MatchProfile matchProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new MatchProfile()
        .withName("ProfileToDelete")
        .withIncomingRecordType(MARC)
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
        .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD))
      .when()
      .post(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MatchProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + matchProfileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .param("showDeleted", true)
      .get(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(4));
  }

  @Test
  public void shouldReturnOnlyUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsNotPassed() {
    createProfiles();
    MatchProfile matchProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new MatchProfile()
        .withName("ProfileToDelete")
        .withIncomingRecordType(MARC)
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
        .withIncomingDataValueType(IncomingDataValueType.VALUE_FROM_INCOMING_RECORD))
      .when()
      .post(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MatchProfile.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + matchProfileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("matchProfiles*.deleted", everyItem(is(false)));
  }

  private void createProfiles() {
    List<MatchProfile> matchProfilesToPost = Arrays.asList(matchProfile_1, matchProfile_2, matchProfile_3);
    for (MatchProfile profile : matchProfilesToPost) {
      RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(MATCH_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(MATCH_TO_MATCH_PROFILES_TABLE, new Criterion(), event ->
      PostgresClient.getInstance(vertx, TENANT_ID).delete(MATCH_PROFILES_TABLE_NAME, new Criterion(), event2 -> {
        if (event.failed()) {
          context.fail(event2.cause());
        }
        async.complete();
      }));
  }
}
