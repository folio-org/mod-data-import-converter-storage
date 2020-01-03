package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.Field;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MatchDetail;
import org.folio.rest.jaxrs.model.MatchExpression;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfile.ExistingRecordType;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.jaxrs.model.Qualifier;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_PATH;
import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_TABLE_NAME;
import static org.folio.rest.impl.ActionProfileTest.actionProfile_1;
import static org.folio.rest.impl.JobProfileTest.JOB_PROFILES_PATH;
import static org.folio.rest.impl.JobProfileTest.jobProfile_1;
import static org.folio.rest.impl.association.CommonProfileAssociationTest.ASSOCIATED_PROFILES_URL;
import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.MatchDetail.MatchCriterion.EXACTLY_MATCHES;
import static org.folio.rest.jaxrs.model.MatchExpression.DataValueType.VALUE_FROM_RECORD;
import static org.folio.rest.jaxrs.model.MatchProfile.IncomingRecordType.MARC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;
import static org.folio.rest.jaxrs.model.Qualifier.ComparisonPart.NUMERICS_ONLY;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(VertxUnitRunner.class)
public class MatchProfileTest extends AbstractRestVerticleTest {

  static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE = "match_to_match_profiles";
  public static final String MATCH_TO_ACTION_PROFILES_TABLE = "match_to_action_profiles";
  static final String MATCH_PROFILES_PATH = "/data-import-profiles/matchProfiles";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";

  private static MatchProfile matchProfile_1 = new MatchProfile().withName("Bla")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC);
  private static MatchProfile matchProfile_2 = new MatchProfile().withName("Boo")
    .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC);
  private static MatchProfile matchProfile_3 = new MatchProfile().withName("Foo")
    .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
    .withIncomingRecordType(MARC)
    .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC);

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
      .get(MATCH_PROFILES_PATH + "?withRelations=true")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("matchProfiles*.deleted", everyItem(is(false)));
  }

  @Test
  public void shouldReturnAllProfilesOnGetTree() {
    List<String> ids = createProfiles();
    createProfilesTree(ids);
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "?withRelations=true&query=id="+ids.get(0))
      .then()
      .statusCode(HttpStatus.SC_OK).log().all()
      .body("totalRecords", is(1))
      .body("matchProfiles*.childProfiles*.id", everyItem(is(notNullValue())))
      .body("matchProfiles*.parentProfiles*.id", everyItem(is(notNullValue())))
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
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC))
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
  public void shouldDeleteAssociationsWithDetailProfilesOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_1)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile profileToDelete = createResponse.body().as(MatchProfile.class);

    // creation detail-profiles
    createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile_2)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile associatedMatchProfile = createResponse.body().as(MatchProfile.class);

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

    // creation associations
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(profileToDelete.getId())
      .withOrder(1);

    ProfileAssociation matchToMatchAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedMatchProfile.getId()),
      MATCH_PROFILE, MATCH_PROFILE);
    ProfileAssociation matchToActionAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedActionProfile.getId()),
      MATCH_PROFILE, ACTION_PROFILE);

    // deleting match profile
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MATCH_PROFILES_PATH + "/" + profileToDelete.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    // receiving deleted associations
    RestAssured.given()
      .spec(spec)
      .queryParam("master", MATCH_PROFILE.value())
      .queryParam("detail", MATCH_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + matchToMatchAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", MATCH_PROFILE.value())
      .queryParam("detail", ACTION_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + matchToActionAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    MatchProfile matchProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new MatchProfile()
        .withName("ProfileToDelete")
        .withIncomingRecordType(MARC)
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC))
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
        .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC))
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

  @Test
  public void shouldCreateProfileWithMatchDetailsOnPost() {
    MatchDetail matchDetail = new MatchDetail()
      .withIncomingRecordType(MARC)
      .withExistingRecordType(ExistingRecordType.INSTANCE)
      .withIncomingMatchExpression(new MatchExpression()
        .withDataValueType(VALUE_FROM_RECORD)
        .withFields(Arrays.asList(
          new Field().withLabel("field").withValue("001"),
          new Field().withLabel("indicator1").withValue(StringUtils.EMPTY),
          new Field().withLabel("indicator2").withValue(StringUtils.EMPTY),
          new Field().withLabel("recordSubfield").withValue(StringUtils.EMPTY)))
        .withQualifier(new Qualifier().withComparisonPart(NUMERICS_ONLY)))
      .withMatchCriterion(EXACTLY_MATCHES)
      .withExistingMatchExpression(new MatchExpression()
        .withDataValueType(VALUE_FROM_RECORD)
        .withFields(Collections.singletonList(
          new Field().withLabel("field").withValue("INSTANCE_HRID")))
        .withQualifier(new Qualifier().withComparisonPart(NUMERICS_ONLY)));

    MatchProfile matchProfile = new MatchProfile()
      .withName("Bla")
      .withTags(new Tags().withTagList(Collections.singletonList("hrid")))
      .withIncomingRecordType(MARC)
      .withExistingRecordType(ExistingRecordType.MARC_BIBLIOGRAPHIC)
      .withMatchDetails(Collections.singletonList(matchDetail));

    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(matchProfile)
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfile createdMatchProfile = createResponse.body().as(MatchProfile.class);

    Response getResponse = RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "/" + createdMatchProfile.getId());
    Assert.assertThat(getResponse.statusCode(), is(HttpStatus.SC_OK));
    MatchProfile receivedMatchProfile = getResponse.body().as(MatchProfile.class);

    // assert id and name
    Assert.assertThat(receivedMatchProfile.getId(), is(createdMatchProfile.getId()));
    Assert.assertThat(receivedMatchProfile.getName(), is(createdMatchProfile.getName()));

    // assert matchDetail
    Assert.assertThat(receivedMatchProfile.getMatchDetails().size(), is(1));
    MatchDetail receivedMatchDetail1 = receivedMatchProfile.getMatchDetails().get(0);
    Assert.assertThat(receivedMatchDetail1.getIncomingRecordType(), is(matchDetail.getIncomingRecordType()));
    Assert.assertThat(receivedMatchDetail1.getExistingRecordType(), is(matchDetail.getExistingRecordType()));

    // assert incomingMatchExpression
    Assert.assertThat(receivedMatchDetail1.getIncomingMatchExpression().getDataValueType(),
      is(matchDetail.getIncomingMatchExpression().getDataValueType()));
    Assert.assertThat(receivedMatchDetail1.getIncomingMatchExpression().getQualifier().getComparisonPart(),
      is(matchDetail.getIncomingMatchExpression().getQualifier().getComparisonPart()));

    // assert incoming fields
    Assert.assertThat(receivedMatchDetail1.getIncomingMatchExpression().getFields().size(), is(4));
    List<Field> createdIncomingFields = receivedMatchDetail1.getIncomingMatchExpression().getFields();
    for (int i = 0; i < createdIncomingFields.size(); i++) {
      Assert.assertThat(createdIncomingFields.get(i).getLabel(),
        is(matchDetail.getIncomingMatchExpression().getFields().get(i).getLabel()));
      Assert.assertThat(createdIncomingFields.get(i).getValue(),
        is(matchDetail.getIncomingMatchExpression().getFields().get(i).getValue()));
    }

    // assert matchCriterion
    Assert.assertThat(receivedMatchDetail1.getMatchCriterion(), is(matchDetail.getMatchCriterion()));

    // assert existingMatchExpression
    Assert.assertThat(receivedMatchDetail1.getExistingMatchExpression().getDataValueType(),
      is(matchDetail.getExistingMatchExpression().getDataValueType()));
    Assert.assertThat(receivedMatchDetail1.getExistingMatchExpression().getFields().size(), is(1));
    Assert.assertThat(receivedMatchDetail1.getExistingMatchExpression().getFields().get(0).getLabel(),
      is(matchDetail.getExistingMatchExpression().getFields().get(0).getLabel()));
    Assert.assertThat(receivedMatchDetail1.getExistingMatchExpression().getFields().get(0).getValue(),
      is(matchDetail.getExistingMatchExpression().getFields().get(0).getValue()));
    Assert.assertThat(receivedMatchDetail1.getExistingMatchExpression().getQualifier().getComparisonPart(),
      is(matchDetail.getExistingMatchExpression().getQualifier().getComparisonPart()));
  }

  private List<String> createProfiles() {
    List<MatchProfile> matchProfilesToPost = Arrays.asList(matchProfile_1, matchProfile_2, matchProfile_3);
    List<String> ids = new ArrayList<>();
    for (MatchProfile profile : matchProfilesToPost) {
      ids.add(RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(MATCH_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED).extract().body().as(MatchProfile.class).getId());
    }
    return ids;
  }

  private void createProfilesTree(List<String> profilesIds) {
    String nameForProfiles = "tree";
    List<JobProfile> jobProfiles = Arrays.asList(jobProfile_1, jobProfile_1, jobProfile_1);
    List<ActionProfile> actionProfiles = Arrays.asList(actionProfile_1, actionProfile_1, actionProfile_1);
    List<JobProfile> createdJobs = new ArrayList<>();
    List<ActionProfile> createdActions = new ArrayList<>();
    int i = 0;
    for (JobProfile profile : jobProfiles) {
      createdJobs.add(RestAssured.given()
        .spec(spec)
        .body(profile.withName(nameForProfiles + i))
        .when()
        .post(JOB_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED).extract().body().as(JobProfile.class));
      i++;
    }
    for (ActionProfile action : actionProfiles) {
      createdActions.add(RestAssured.given()
        .spec(spec)
        .body(action.withName(nameForProfiles + i))
        .when()
        .post(ACTION_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED).extract().body().as(ActionProfile.class));
      i++;
    }
    for (int j = 0; j < profilesIds.size(); j++) {
      ProfileAssociation associationChild = new ProfileAssociation();
      ProfileAssociation associationParent = new ProfileAssociation();
      associationChild.setMasterProfileId(profilesIds.get(j));
      associationChild.setDetailProfileId(createdActions.get(j).getId());
      associationParent.setDetailProfileId(profilesIds.get(j));
      associationParent.setMasterProfileId(createdJobs.get(j).getId());
      RestAssured.given()
        .spec(spec)
        .queryParam("master", MATCH_PROFILE)
        .queryParam("detail", ACTION_PROFILE)
        .body(associationChild)
        .when()
        .post(ASSOCIATED_PROFILES_URL)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
      RestAssured.given()
        .spec(spec)
        .queryParam("detail", MATCH_PROFILE)
        .queryParam("master", JOB_PROFILE)
        .body(associationParent)
        .when()
        .post(ASSOCIATED_PROFILES_URL)
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
    pgClient.delete(MATCH_TO_ACTION_PROFILES_TABLE, new Criterion(), event ->
      pgClient.delete(MATCH_TO_MATCH_PROFILES_TABLE, new Criterion(), event2 ->
        pgClient.delete(MATCH_PROFILES_TABLE_NAME, new Criterion(), event3 ->
          pgClient.delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), event4 -> {
            if (event4.failed()) {
              context.fail(event4.cause());
            }
            async.complete();
          }))));
  }
}
