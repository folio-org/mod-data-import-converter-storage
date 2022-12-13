package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.ArrayList;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.EntityType;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.JobProfileUpdateDto;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileAssociation;
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
import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.JobProfile.DataType.*;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
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
  static final String JOB_PROFILES_PATH = "/data-import-profiles/jobProfiles";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";
  static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";
  private static final String ACTION_TO_MAPPING_PROFILES_TABLE = "action_to_mapping_profiles";
  static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String SNAPSHOTS_TABLE_NAME = "profile_snapshots";
  private static final String MATCH_TO_ACTION_PROFILES_TABLE_NAME = "match_to_action_profiles";
  private static final String ACTION_TO_ACTION_PROFILES_TABLE_NAME = "action_to_action_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE_NAME = "match_to_match_profiles";
  private static final String JOB_PROFILE_UUID = "b81c283c-131d-4470-ab91-e92bb415c000";
  private static final String DEFAULT_CREATE_SRS_MARC_AUTHORITY_JOB_PROFILE_ID = "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3";
  private final List<String> defaultJobProfileIds = Arrays.asList(
    "d0ebb7b0-2f0f-11eb-adc1-0242ac120002", //OCLC_CREATE_INSTANCE_JOB_PROFILE_ID
    "91f9b8d6-d80e-4727-9783-73fb53e3c786", //OCLC_UPDATE_INSTANCE_JOB_PROFILE_ID
    "fa0262c7-5816-48d0-b9b3-7b7a862a5bc7", //DEFAULT_CREATE_DERIVE_HOLDINGS_JOB_PROFILE_ID
    "6409dcff-71fa-433a-bc6a-e70ad38a9604", //DEFAULT_CREATE_DERIVE_INSTANCE_JOB_PROFILE_ID
    "80898dee-449f-44dd-9c8e-37d5eb469b1d", //DEFAULT_CREATE_HOLDINGS_AND_SRS_MARC_HOLDINGS_JOB_PROFILE_ID
    "1a338fcd-3efc-4a03-b007-394eeb0d5fb9", //DEFAULT_DELETE_MARC_AUTHORITY_JOB_PROFILE_ID
    "cf6f2718-5aa5-482a-bba5-5bc9b75614da", //DEFAULT_QM_MARC_BIB_UPDATE_JOB_PROFILE_ID
    "6cb347c6-c0b0-4363-89fc-32cedede87ba", //DEFAULT_QM_HOLDINGS_UPDATE_JOB_PROFILE_ID
    "c7fcbc40-c4c0-411d-b569-1fc6bc142a92" //DEFAULT_QM_AUTHORITY_UPDATE_JOB_PROFILE_ID
  );

  static JobProfileUpdateDto jobProfile_1 = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withName("Bla")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withDataType(MARC));
  static JobProfileUpdateDto jobProfile_2 = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withName("Boo")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
      .withDataType(MARC));
  static JobProfileUpdateDto jobProfile_3 = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withName("Foo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withDataType(MARC));
  static JobProfileUpdateDto jobProfile_4 = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withId(JOB_PROFILE_UUID)
      .withName("OLA")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withDataType(MARC));
  static JobProfileUpdateDto jobProfile_5 = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withId(DEFAULT_CREATE_SRS_MARC_AUTHORITY_JOB_PROFILE_ID)
      .withName("Default - Create SRS MARC Authority")
      .withDescription("Default job profile for creating MARC authority records.")
      .withDataType(MARC));

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
      .get(JOB_PROFILES_PATH + "?withRelations=true")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("jobProfiles*.deleted", everyItem(is(false)))
      .body("jobProfiles*.hidden", everyItem(is(false)));
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
      .body("jobProfiles*.hidden", everyItem(is(false)))
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
      .body("jobProfiles*.hidden", everyItem(is(false)))
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
      .body("jobProfiles*.hidden", everyItem(is(false)))
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
  public void shouldReturnBadRequestOnDeleteDefaultProfiles() {
    createProfiles();
    List<String> allDefaultJobProfilesIds = new ArrayList<>(defaultJobProfileIds);
    allDefaultJobProfilesIds.add(DEFAULT_CREATE_SRS_MARC_AUTHORITY_JOB_PROFILE_ID);
    for (String id : allDefaultJobProfilesIds) {
      RestAssured.given()
        .spec(spec)
        .when()
        .delete(JOB_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
  }

  @Test
  public void shouldReturnBadRequestOnPutDefaultProfiles() {
    createProfiles();
    for (String id : defaultJobProfileIds) {
      RestAssured.given()
        .spec(spec)
        .body(jobProfile_1)
        .when()
        .put(JOB_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
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
      .body("profile.name", is(jobProfile_1.getProfile().getName()))
      .body("profile.tags.tagList", is(jobProfile_1.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"))
      .body("profile.dataType", is(jobProfile_1.getProfile().getDataType().value()));

    RestAssured.given().spec(spec)
      .body(jobProfile_1)
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors[0].message", is("jobProfile.duplication.invalid"));
  }

  @Test
  public void shouldCreateProfileWithGivenIdOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(jobProfile_4)
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(jobProfile_4.getProfile().getName()))
      .body("profile.tags.tagList", is(jobProfile_4.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"))
      .body("profile.dataType", is(jobProfile_4.getProfile().getDataType().value()));

    RestAssured.given()
      .spec(spec)
      .body(new JobProfileUpdateDto()
        .withProfile(new JobProfile().withId(JOB_PROFILE_UUID)
          .withName("GOA")
          .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
          .withDataType(MARC)))
      .when()
      .post(JOB_PROFILES_PATH)
      .then().log().all()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors[0].message", is("jobProfile.duplication.id"));
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
    JobProfileUpdateDto jobProfile = createResponse.body().as(JobProfileUpdateDto.class);

    jobProfile.getProfile().setDescription("test");
    jobProfile.getProfile().setDataType(DELIMITED);
    RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + jobProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(jobProfile.getProfile().getId()))
      .body("name", is(jobProfile.getProfile().getName()))
      .body("description", is("test"))
      .body("tags.tagList", is(jobProfile.getProfile().getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"))
      .body("dataType", is(jobProfile.getProfile().getDataType().value()));
  }

  @Test
  public void shouldUpdateDefaultAuthorityJobProfileOnPut() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_5)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfileUpdateDto jobProfile = createResponse.body().as(JobProfileUpdateDto.class);

    jobProfile.getProfile().setName("updated name");
    jobProfile.getProfile().setDescription("updated description");
    jobProfile.getProfile().setDataType(EDIFACT);

    RestAssured.given()
      .spec(spec)
      .body(jobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + DEFAULT_CREATE_SRS_MARC_AUTHORITY_JOB_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(DEFAULT_CREATE_SRS_MARC_AUTHORITY_JOB_PROFILE_ID))
      .body("name", is(jobProfile.getProfile().getName()))
      .body("description", is(jobProfile.getProfile().getDescription()))
      .body("dataType", is(jobProfile.getProfile().getDataType().value()));
  }

  @Test
  public void shouldReturnBadRequestOnPutJobProfileWithInvalidField() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(jobProfile_2)
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfileUpdateDto jobProfile = createResponse.body().as(JobProfileUpdateDto.class);

    JsonObject jobProfileJson = JsonObject.mapFrom(jobProfile)
      .put("invalidField", "value");

    RestAssured.given()
      .spec(spec)
      .body(jobProfileJson.encode())
      .when()
      .put(JOB_PROFILES_PATH + "/" + jobProfile.getProfile().getId())
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
    JobProfileUpdateDto jobProfile = createResponse.body().as(JobProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + jobProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(jobProfile.getProfile().getId()))
      .body("name", is(jobProfile.getProfile().getName()))
      .body("tags.tagList", is(jobProfile.getProfile().getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"))
      .body("dataType", is(jobProfile.getProfile().getDataType().value()));
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
    JobProfileUpdateDto profile = createResponse.body().as(JobProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + profile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + profile.getProfile().getId())
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
    JobProfileUpdateDto profileToDelete = createResponse.body().as(JobProfileUpdateDto.class);

    // creation detail-profiles
    createResponse = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("testAction")
          .withAction(CREATE)
          .withFolioRecord(MARC_BIBLIOGRAPHIC)))
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto associatedActionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(new MatchProfileUpdateDto()
        .withProfile(new MatchProfile()
          .withName("testMatch")
          .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
          .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MATCH_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MatchProfileUpdateDto associatedMatchProfile = createResponse.body().as(MatchProfileUpdateDto.class);

    // creation associations
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(profileToDelete.getProfile().getId())
      .withOrder(1);

    ProfileAssociation jobToActionAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedActionProfile.getProfile().getId()),
      JOB_PROFILE, ACTION_PROFILE);
    ProfileAssociation jobToMatchAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedMatchProfile.getProfile().getId()),
      JOB_PROFILE, MATCH_PROFILE);

    // deleting job profile
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
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
      .body(new JobProfileUpdateDto().withProfile(newJobProfile))
      .when()
      .post(JOB_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    JobProfileUpdateDto createdJobProfile = createResponse.body().as(JobProfileUpdateDto.class);

    createdJobProfile.getProfile().setName(jobProfile_1.getProfile().getName());
    RestAssured.given()
      .spec(spec)
      .body(createdJobProfile)
      .when()
      .put(JOB_PROFILES_PATH + "/" + createdJobProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    JobProfileUpdateDto jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new JobProfileUpdateDto().withProfile(new JobProfile().withName("ProfileToDelete")
        .withDataType(MARC)))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getProfile().getId())
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
    JobProfileUpdateDto jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new JobProfileUpdateDto().withProfile(new JobProfile().withName("ProfileToDelete")
        .withDataType(MARC)))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getProfile().getId())
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
      .body("jobProfiles*.deleted", everyItem(is(false)))
      .body("jobProfiles*.hidden", everyItem(is(false)));
  }

  @Test
  public void shouldCreateProfileOnPostWhenWasDeletedProfileWithSameNameBefore() {
    JobProfile jobProfile = new JobProfile().withName("profileName")
      .withDataType(MARC);

    JobProfileUpdateDto jobProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new JobProfileUpdateDto().withProfile(jobProfile))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(JobProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(JOB_PROFILES_PATH + "/" + jobProfileToDelete.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .body(new JobProfileUpdateDto().withProfile(jobProfile))
      .when()
      .post(JOB_PROFILES_PATH)
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED);
  }

  private void createProfiles() {
    List<JobProfileUpdateDto> jobProfilesToPost = Arrays.asList(jobProfile_1, jobProfile_2, jobProfile_3);
    for (JobProfileUpdateDto profile : jobProfilesToPost) {
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
    pgClient.delete(SNAPSHOTS_TABLE_NAME, new Criterion(), event2 ->
      pgClient.delete(JOB_TO_ACTION_PROFILES_TABLE, new Criterion(), event3 ->
        pgClient.delete(JOB_TO_MATCH_PROFILES_TABLE, new Criterion(), event4 ->
          pgClient.delete(ACTION_TO_MAPPING_PROFILES_TABLE, new Criterion(), event5 ->
            pgClient.delete(MATCH_TO_ACTION_PROFILES_TABLE_NAME, new Criterion(), event6 ->
              pgClient.delete(JOB_PROFILES_TABLE_NAME, new Criterion(), event7 ->
                pgClient.delete(MATCH_PROFILES_TABLE_NAME, new Criterion(), event8 ->
                  pgClient.delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), event9 ->
                    pgClient.delete(ACTION_TO_ACTION_PROFILES_TABLE_NAME, new Criterion(), event10 ->
                      pgClient.delete(MAPPING_PROFILES_TABLE_NAME, new Criterion(), event11 ->
                        pgClient.delete(MATCH_TO_MATCH_PROFILES_TABLE_NAME, new Criterion(), event12 -> {
                          if (event12.failed()) {
                            context.fail(event12.cause());
                          }
                          async.complete();
                        })))))))))));
  }
}
