package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.EntityType;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
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

import static org.folio.rest.impl.MappingProfileTest.MAPPING_PROFILES_PATH;
import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.INSTANCE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class ActionProfileTest extends AbstractRestVerticleTest {

  static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String ACTION_TO_ACTION_PROFILES_TABLE_NAME = "action_to_action_profiles";
  private static final String ACTION_TO_MAPPING_PROFILES_TABLE_NAME = "action_to_mapping_profiles";
  static final String ACTION_PROFILES_PATH = "/data-import-profiles/actionProfiles";
  private static final String ENTITY_TYPES_PATH = " /data-import-profiles/entityTypes";
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  public static final String JOB_TO_ACTION_PROFILES_TABLE_NAME = "job_to_action_profiles";
  public static final String JOB_TO_MATCH_PROFILES_TABLE_NAME = "job_to_match_profiles";
  static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";
  static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String SNAPSHOTS_TABLE_NAME = "profile_snapshots";
  private static final String MATCH_TO_ACTION_PROFILES_TABLE_NAME = "match_to_action_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE_NAME = "match_to_match_profiles";
  private static final String ACTION_PROFILE_UUID = "16449d21-ad7c-4f69-b31e-a521fe4ae893";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";
  private List<String> defaultActionProfileIds = Arrays.asList(
    "d0ebba8a-2f0f-11eb-adc1-0242ac120002", //OCLC_CREATE_INSTANCE_ACTION_PROFILE_ID
    "cddff0e1-233c-47ba-8be5-553c632709d9", //OCLC_UPDATE_INSTANCE_ACTION_PROFILE_ID
    "6aa8e98b-0d9f-41dd-b26f-15658d07eb52", //OCLC_UPDATE_MARC_BIB_ACTION_PROFILE_ID
    "f8e58651-f651-485d-aead-d2fa8700e2d1", //DEFAULT_CREATE_DERIVE_INSTANCE_ACTION_PROFILE_ID
    "f5feddba-f892-4fad-b702-e4e77f04f9a3", //DEFAULT_CREATE_DERIVE_HOLDINGS_ACTION_PROFILE_ID
    "8aa0b850-9182-4005-8435-340b704b2a19", //DEFAULT_CREATE_HOLDINGS_ACTION_PROFILE_ID
    "7915c72e-c6af-4962-969d-403c7238b051", //DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID
    "fabd9a3e-33c3-49b7-864d-c5af830d9990"  //DEFAULT_DELETE_MARC_AUTHORITY_ACTION_PROFILE_ID
  );

  static ActionProfileUpdateDto actionProfile_1 = new ActionProfileUpdateDto()
    .withProfile(new ActionProfile().withName("Bla")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withAction(CREATE)
      .withFolioRecord(MARC_BIBLIOGRAPHIC));
  static ActionProfileUpdateDto actionProfile_2 = new ActionProfileUpdateDto()
    .withProfile(new ActionProfile().withName("Boo")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
      .withAction(CREATE)
      .withFolioRecord(MARC_BIBLIOGRAPHIC));
  static ActionProfileUpdateDto actionProfile_3 = new ActionProfileUpdateDto()
    .withProfile(new ActionProfile().withName("Foo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withAction(CREATE)
      .withFolioRecord(MARC_BIBLIOGRAPHIC));
  static ActionProfileUpdateDto actionProfile_4 = new ActionProfileUpdateDto()
    .withProfile(new ActionProfile().withId(ACTION_PROFILE_UUID).withName("OLA")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withAction(CREATE)
      .withFolioRecord(MARC_BIBLIOGRAPHIC));

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
      .get(ACTION_PROFILES_PATH + "?withRelations=true")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("actionProfiles*.deleted", everyItem(is(false)))
      .body("actionProfiles*.hidden", everyItem(is(false)));
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
      .body("actionProfiles*.hidden", everyItem(is(false)))
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
      .body("actionProfiles*.hidden", everyItem(is(false)))
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
      .body("profile.name", is(actionProfile_1.getProfile().getName()))
      .body("profile.tags.tagList", is(actionProfile_1.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(actionProfile_1)
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileWithGivenIdOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(actionProfile_4)
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(actionProfile_4.getProfile().getName()))
      .body("profile.tags.tagList", is(actionProfile_4.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile().withId(ACTION_PROFILE_UUID).withName("GOA")
          .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
          .withAction(CREATE)
          .withFolioRecord(MARC_BIBLIOGRAPHIC)))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors[0].message", is("actionProfile.duplication.id"));
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
  public void shouldReturnBadRequestOnPutWithDefaultProfiles() {
    createProfiles();
    for (String id : defaultActionProfileIds) {
      RestAssured.given()
        .spec(spec)
        .body(actionProfile_1)
        .when()
        .put(ACTION_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
  }

  @Test
  public void shouldReturnBadRequestOnDeleteWithDefaultProfiles() {
    createProfiles();
    for (String id : defaultActionProfileIds) {
      RestAssured.given()
        .spec(spec)
        .when()
        .delete(ACTION_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
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
      .body(new ActionProfileUpdateDto().withProfile(new ActionProfile()
        .withName("newProfile")
        .withAction(CREATE)
        .withFolioRecord(INSTANCE)))
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto createdProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    createdProfile.getProfile().setName(actionProfile_1.getProfile().getName());
    RestAssured.given()
      .spec(spec)
      .body(createdProfile)
      .when()
      .put(ACTION_PROFILES_PATH + "/" + createdProfile.getProfile().getId())
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
    ActionProfileUpdateDto actionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    actionProfile.getProfile().setDescription("test");
    RestAssured.given()
      .spec(spec)
      .body(actionProfile)
      .when()
      .put(ACTION_PROFILES_PATH + "/" + actionProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(actionProfile.getProfile().getId()))
      .body("description", is("test"))
      .body("name", is(actionProfile.getProfile().getName()))
      .body("tags.tagList", is(actionProfile.getProfile().getTags().getTagList()))
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
    ActionProfileUpdateDto actionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + actionProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(actionProfile.getProfile().getId()))
      .body("name", is(actionProfile.getProfile().getName()))
      .body("tags.tagList", is(actionProfile.getProfile().getTags().getTagList()))
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
    ActionProfileUpdateDto profileToDelete = createResponse.body().as(ActionProfileUpdateDto.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto associatedActionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", ACTION_PROFILE.value())
      .body(new ProfileAssociation()
        .withMasterProfileId(associatedActionProfile.getProfile().getId())
        .withDetailProfileId(profileToDelete.getProfile().getId())
        .withOrder(1))
      .when()
      .post(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED));

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
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
    ActionProfileUpdateDto profile = createResponse.body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + profile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("deleted", is(true));
  }

  @Test
  public void shouldDeleteAssociationsWithDetailProfilesOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_1)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto profileToDelete = createResponse.body().as(ActionProfileUpdateDto.class);

    // creation detail-profiles
    createResponse = RestAssured.given()
      .spec(spec)
      .body(actionProfile_2)
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto associatedActionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(new MappingProfile().withName("testMapping")
        .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
        .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto associatedMappingProfile = createResponse.body().as(MappingProfileUpdateDto.class);

    // creation associations
    ProfileAssociation profileAssociation = new ProfileAssociation()
      .withMasterProfileId(profileToDelete.getProfile().getId())
      .withOrder(1);

    ProfileAssociation actionToActionAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedActionProfile.getProfile().getId()),
      ACTION_PROFILE, ACTION_PROFILE);
    ProfileAssociation actionToMappingAssociation = postProfileAssociation(profileAssociation.withDetailProfileId(associatedMappingProfile.getProfile().getId()),
      ACTION_PROFILE, MAPPING_PROFILE);

    // deleting action profile
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    // receiving deleted associations
    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", ACTION_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + actionToActionAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH + "/" + actionToMappingAssociation.getId())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    ActionProfileUpdateDto profileToDelete = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto().withProfile(new ActionProfile()
        .withName("ProfileToDelete")
        .withAction(CREATE)
        .withFolioRecord(INSTANCE)))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
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
    ActionProfileUpdateDto profileToDelete = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto().withProfile(new ActionProfile()
        .withName("ProfileToDelete")
        .withAction(CREATE)
        .withFolioRecord(INSTANCE)))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(ACTION_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("actionProfiles*.deleted", everyItem(is(false)))
      .body("actionProfiles*.hidden", everyItem(is(false)));
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

  @Test
  public void shouldNotCreateActionProfilesWhenDifferentFolioRecord() {
    var mappingProfileUpdateDto = postMappingProfile(new MappingProfileUpdateDto()
      .withProfile(new MappingProfile()
        .withName("Test Mapping Profile")
        .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
        .withExistingRecordType(EntityType.ITEM)
        .withIncomingRecordType(EntityType.HOLDINGS)));

    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("Test Action Profile")
          .withAction(CREATE)
          .withFolioRecord(MARC_BIBLIOGRAPHIC))
        .withAddedRelations(List.of(
          new ProfileAssociation()
            .withDetailProfileType(ProfileAssociation.DetailProfileType.MAPPING_PROFILE)
            .withDetailProfileId(mappingProfileUpdateDto.getId())
            .withMasterProfileType(ProfileAssociation.MasterProfileType.ACTION_PROFILE))))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Mapping profile 'Test Mapping Profile' can not be linked to this Action profile. ExistingRecordType and FolioRecord types are different")
        )));
  }

  @Test
  public void shouldNotUpdateActionProfilesWhenDifferentFolioRecord() {
    var mappingProfileUpdateDto = postMappingProfile(new MappingProfileUpdateDto()
      .withProfile(new MappingProfile()
        .withName("Test Mapping Profile")
        .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
        .withExistingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
        .withIncomingRecordType(EntityType.HOLDINGS)));
    var mappingProfileUpdateDto1 = postMappingProfile(new MappingProfileUpdateDto()
      .withProfile(new MappingProfile()
        .withName("Test Mapping Profile1")
        .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
        .withExistingRecordType(EntityType.HOLDINGS)
        .withIncomingRecordType(EntityType.HOLDINGS)));

    var actionProfileUpdateDto = postActionProfile(new ActionProfileUpdateDto()
      .withProfile(new ActionProfile()
        .withName("Test Action Profile")
        .withAction(CREATE)
        .withFolioRecord(MARC_BIBLIOGRAPHIC))
      .withAddedRelations(List.of(
        new ProfileAssociation()
          .withDetailProfileType(ProfileAssociation.DetailProfileType.MAPPING_PROFILE)
          .withDetailProfileId(mappingProfileUpdateDto.getProfile().getId())
          .withMasterProfileType(ProfileAssociation.MasterProfileType.ACTION_PROFILE))));

    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("Test Action Profile")
          .withAction(CREATE)
          .withFolioRecord(MARC_BIBLIOGRAPHIC))
        .withAddedRelations(List.of(
          new ProfileAssociation()
            .withDetailProfileType(ProfileAssociation.DetailProfileType.MAPPING_PROFILE)
            .withDetailProfileId(mappingProfileUpdateDto1.getProfile().getId())
            .withMasterProfileType(ProfileAssociation.MasterProfileType.ACTION_PROFILE)))
        .withDeletedRelations(List.of(
          new ProfileAssociation()
            .withDetailProfileType(ProfileAssociation.DetailProfileType.MAPPING_PROFILE)
            .withDetailProfileId(mappingProfileUpdateDto.getProfile().getId())
            .withMasterProfileType(ProfileAssociation.MasterProfileType.ACTION_PROFILE))))
      .when()
      .put(ACTION_PROFILES_PATH+ "/" + actionProfileUpdateDto.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Mapping profile 'Test Mapping Profile1' can not be linked to this Action profile. ExistingRecordType and FolioRecord types are different")
        )));

    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("Test Action Profile")
          .withAction(CREATE)
          .withFolioRecord(INSTANCE)
          .withChildProfiles(List.of(new ProfileSnapshotWrapper()
            .withId(mappingProfileUpdateDto.getId())
            .withContent(mappingProfileUpdateDto)
            .withContentType(MAPPING_PROFILE)))))
      .when()
      .put(ACTION_PROFILES_PATH+ "/" + actionProfileUpdateDto.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Can not update ActionProfile recordType and linked MappingProfile recordType are different")
        )));
  }

  private void createProfiles() {
    List<ActionProfileUpdateDto> actionProfilesToPost = Arrays.asList(actionProfile_1, actionProfile_2, actionProfile_3);
    for (ActionProfileUpdateDto profile : actionProfilesToPost) {
      RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(ACTION_PROFILES_PATH)
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
        pgClient.delete(JOB_TO_ACTION_PROFILES_TABLE_NAME, new Criterion(), event3 ->
          pgClient.delete(JOB_TO_MATCH_PROFILES_TABLE_NAME, new Criterion(), event4 ->
            pgClient.delete(ACTION_TO_MAPPING_PROFILES_TABLE_NAME, new Criterion(), event5 ->
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
