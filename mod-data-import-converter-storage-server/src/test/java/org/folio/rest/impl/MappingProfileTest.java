package org.folio.rest.impl;

import com.google.common.collect.Lists;
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
import org.folio.rest.jaxrs.model.MappingDetail;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.MappingRule;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileAssociation.DetailProfileType;
import org.folio.rest.jaxrs.model.ProfileAssociation.MasterProfileType;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
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
import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.Action.UPDATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.INSTANCE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class MappingProfileTest extends AbstractRestVerticleTest {

  static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";
  private static final String ACTION_TO_MAPPING_PROFILES_TABLE = "action_to_mapping_profiles";
  static final String MAPPING_PROFILES_PATH = "/data-import-profiles/mappingProfiles";
  private static final String ASSOCIATED_PROFILES_PATH = "/data-import-profiles/profileAssociations";
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String JOB_TO_ACTION_PROFILES_TABLE = "job_to_action_profiles";
  private static final String JOB_TO_MATCH_PROFILES_TABLE = "job_to_match_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE = "match_to_match_profiles";
  private static final String MATCH_TO_ACTION_PROFILES_TABLE = "match_to_action_profiles";
  static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String MAPPING_PROFILE_UUID = "608ab35e-5f8b-49c3-bcf1-1fb5e57d5130";
  private List<String> defaultMappingProfileIds = Arrays.asList(
    "d0ebbc2e-2f0f-11eb-adc1-0242ac120002", //OCLC_CREATE_MAPPING_PROFILE_ID
    "862000b9-84ea-4cae-a223-5fc0552f2b42", //OCLC_UPDATE_MAPPING_PROFILE_ID
    "f90864ef-8030-480f-a43f-8cdd21233252", //OCLC_UPDATE_MARC_BIB_MAPPING_PROFILE_ID
    "991c0300-44a6-47e3-8ea2-b01bb56a38cc", //DEFAULT_CREATE_DERIVE_INSTANCE_MAPPING_PROFILE_ID
    "e0fbaad5-10c0-40d5-9228-498b351dbbaa", //DEFAULT_CREATE_DERIVE_HOLDINGS_MAPPING_PROFILE_ID
    "13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a", //DEFAULT_CREATE_HOLDINGS_MAPPING_PROFILE_ID
    "6a0ec1de-68eb-4833-bdbf-0741db25c314"
  );

  private static MappingProfileUpdateDto mappingProfile_1 = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Bla")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE));
  private static MappingProfileUpdateDto mappingProfile_2 = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Boo")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE));
  private static MappingProfileUpdateDto mappingProfile_3 = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Foo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE));
  private static MappingProfileUpdateDto mappingProfile_4 = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withId(MAPPING_PROFILE_UUID).withName("OLA")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE));

  private static MappingProfileUpdateDto mappingProfile_5 = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("B'oom")
      .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum", "dolor")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE));


  private static final List<MappingRule> fields = Lists.newArrayList(new MappingRule()
    .withName("repeatableField")
    .withPath("instance.repeatableField[]")
    .withValue("")
    .withEnabled("true")
    .withRepeatableFieldAction(MappingRule.RepeatableFieldAction.DELETE_EXISTING)
    .withSubfields(Collections.emptyList()));

  private static final MappingProfileUpdateDto mappingProfileWithEmptySubfieldsAndDeleteExistingAction = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Fooooo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE)
      .withMappingDetails(new MappingDetail().withMappingFields(fields)));

  private static final List<MappingRule> fields2 = Lists.newArrayList(new MappingRule()
    .withName("repeatableField")
    .withPath("instance.repeatableField[]")
    .withValue("")
    .withEnabled("true")
    .withRepeatableFieldAction(null)
    .withSubfields(Collections.emptyList()));

  private static final MappingProfileUpdateDto mappingProfileWithEmptySubfieldsAndEmptyAction = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Fooooo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE)
      .withMappingDetails(new MappingDetail().withMappingFields(fields2)));

  private static final List<MappingRule> fields3 = Lists.newArrayList(new MappingRule()
    .withName("repeatableField")
    .withPath("instance.repeatableField[]")
    .withValue("")
    .withEnabled("true")
    .withRepeatableFieldAction(MappingRule.RepeatableFieldAction.EXTEND_EXISTING)
    .withSubfields(Collections.emptyList()));

  private static final MappingProfileUpdateDto mappingProfileWithEmptySubfieldsAndNotDeleteExistingAction = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("Fooooo")
      .withTags(new Tags().withTagList(Collections.singletonList("lorem")))
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.INSTANCE)
      .withMappingDetails(new MappingDetail().withMappingFields(fields3)));

  @Test
  public void shouldReturnEmptyListOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("mappingProfiles", empty());
  }

  @Test
  public void shouldReturnAllProfilesOnGet() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "?withRelations=true")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("mappingProfiles*.deleted", everyItem(is(false)))
      .body("mappingProfiles*.hidden", everyItem(is(false)));
  }

  @Test
  public void shouldReturnCommittedProfilesOnGetWithQueryByLastName() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "?query=userInfo.lastName=Doe")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("mappingProfiles*.userInfo.lastName", everyItem(is("Doe")));
  }

  @Test
  public void shouldReturnIpsumTaggedProfilesOnGetWithQueryByTag() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "?query=tags.tagList=/respectCase/respectAccents \\\"ipsum\\\"")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("mappingProfiles.get(0).tags.tagList", hasItem("ipsum"))
      .body("mappingProfiles.get(1).tags.tagList", hasItem("ipsum"));
  }

  @Test
  public void shouldReturnLimitedCollectionOnGetWithLimit() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "?limit=2")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("mappingProfiles.size()", is(2))
      .body("totalRecords", is(3));
  }

  @Test
  public void shouldReturnBadRequestOnPost() {
    createProfiles();
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPutWithDefaultProfile() {
    createProfiles();
    for (String id : defaultMappingProfileIds) {
      RestAssured.given()
        .spec(spec)
        .body(mappingProfile_1)
        .when()
        .put(MAPPING_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
  }

  @Test
  public void shouldReturnBadRequestOnDeleteWithDefaultProfile() {
    createProfiles();
    for (String id : defaultMappingProfileIds) {
      RestAssured.given()
        .spec(spec)
        .body(mappingProfile_1)
        .when()
        .delete(MAPPING_PROFILES_PATH + "/" + id)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
  }

  @Test
  public void shouldCreateProfileOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_1)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(mappingProfile_1.getProfile().getName()))
      .body("profile.tags.tagList", is(mappingProfile_1.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_1)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileWithGivenIdOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_4)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(mappingProfile_4.getProfile().getName()))
      .body("profile.tags.tagList", is(mappingProfile_4.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile().withId(MAPPING_PROFILE_UUID).withName("OLA")
          .withTags(new Tags().withTagList(Arrays.asList("lorem", "ipsum")))
          .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
          .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors[0].message", is("mappingProfile.duplication.id"));
  }

  @Test
  public void shouldCreateProfileOnPostWithoutRepeatableSubfieldsAndDeleteExistingAction() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfileWithEmptySubfieldsAndDeleteExistingAction)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(mappingProfileWithEmptySubfieldsAndDeleteExistingAction.getProfile().getName()))
      .body("profile.tags.tagList", is(mappingProfileWithEmptySubfieldsAndDeleteExistingAction.getProfile().getTags().getTagList()));
  }

  @Test
  public void shouldCreateProfileOnPostWithoutRepeatableSubfieldsAndEmptyAction() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfileWithEmptySubfieldsAndEmptyAction)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(mappingProfileWithEmptySubfieldsAndEmptyAction.getProfile().getName()))
      .body("profile.tags.tagList", is(mappingProfileWithEmptySubfieldsAndEmptyAction.getProfile().getTags().getTagList()));
  }

  @Test
  public void shouldNotCreateProfileOnPostWithoutRepeatableSubfieldsAndWithoutDeleteExistingAction() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfileWithEmptySubfieldsAndNotDeleteExistingAction)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateProfileOnPostWithApostropheInName() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_5)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is(mappingProfile_5.getProfile().getName()))
      .body("profile.tags.tagList", is(mappingProfile_5.getProfile().getTags().getTagList()))
      .body("profile.userInfo.lastName", is("Doe"))
      .body("profile.userInfo.firstName", is("Jane"))
      .body("profile.userInfo.userName", is("@janedoe"));

    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_5)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnNotFoundOnPut() {
    RestAssured.given()
      .spec(spec)
      .body(mappingProfile_2)
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnUnprocessableEntityOnPutProfileWithExistingName() {
    createProfiles();

    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(new MappingProfile()
        .withName("newProfile")
        .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
        .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto createdProfile = createResponse.body().as(MappingProfileUpdateDto.class);

    createdProfile.getProfile().setName(mappingProfile_1.getProfile().getName());
    RestAssured.given()
      .spec(spec)
      .body(createdProfile)
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + createdProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldUpdateProfileOnPut() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_2)
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto mappingProfile = createResponse.body().as(MappingProfileUpdateDto.class);

    mappingProfile.getProfile().setDescription("test");
    RestAssured.given()
      .spec(spec)
      .body(mappingProfile)
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + mappingProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(mappingProfile.getProfile().getId()))
      .body("name", is(mappingProfile.getProfile().getName()))
      .body("description", is("test"))
      .body("tags.tagList", is(mappingProfile.getProfile().getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));
  }

  @Test
  public void shouldReturnNotFoundOnGetById() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnProfileOnGetById() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_3)
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto mappingProfile = createResponse.body().as(MappingProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "/" + mappingProfile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(mappingProfile.getProfile().getId()))
      .body("name", is(mappingProfile.getProfile().getName()))
      .body("tags.tagList", is(mappingProfile.getProfile().getTags().getTagList()))
      .body("userInfo.lastName", is("Doe"))
      .body("userInfo.firstName", is("Jane"))
      .body("userInfo.userName", is("@janedoe"));
  }

  @Test
  public void shouldReturnNotFoundOnDelete() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MAPPING_PROFILES_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnBadRequestOnDeleteProfileAssociatedWithOtherProfiles() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_1)
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto profileToDelete = createResponse.body().as(MappingProfileUpdateDto.class);

    createResponse = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto().withProfile(new ActionProfile()
        .withName("testActionProfile")
        .withAction(CREATE)
        .withFolioRecord(MARC_BIBLIOGRAPHIC)))
      .when()
      .post(ACTION_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    ActionProfileUpdateDto actionProfile = createResponse.body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .body(new ProfileAssociation()
        .withMasterProfileId(actionProfile.getProfile().getId())
        .withDetailProfileId(profileToDelete.getProfile().getId())
        .withOrder(1))
      .when()
      .post(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(is(HttpStatus.SC_CREATED));

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MAPPING_PROFILES_PATH + "/" + profileToDelete.getProfile().getId())
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CONFLICT);
  }

  @Test
  public void shouldMarkedProfileAsDeletedOnDelete() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_2)
      .when()
      .post(MAPPING_PROFILES_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MappingProfileUpdateDto profile = createResponse.body().as(MappingProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MAPPING_PROFILES_PATH + "/" + profile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "/" + profile.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("deleted", is(true));
  }

  @Test
  public void shouldReturnMarkedAndUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsTrue() {
    createProfiles();
    MappingProfileUpdateDto mappingProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(new MappingProfile()
        .withName("ProfileToDelete")
        .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
        .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MappingProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MAPPING_PROFILES_PATH + "/" + mappingProfileToDelete.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .param("showDeleted", true)
      .get(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(4));
  }

  @Test
  public void shouldReturnOnlyUnmarkedAsDeletedProfilesOnGetWhenParameterDeletedIsNotPassed() {
    createProfiles();
    MappingProfileUpdateDto mappingProfileToDelete = RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(new MappingProfile()
        .withName("ProfileToDelete")
        .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
        .withExistingRecordType(EntityType.INSTANCE)))
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MappingProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(MAPPING_PROFILES_PATH + "/" + mappingProfileToDelete.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(3))
      .body("mappingProfiles*.deleted", everyItem(is(false)))
      .body("mappingProfiles*.hidden", everyItem(is(false)));
  }

  @Test
  public void shouldCreateProfileOnPostAndReplaceExistingAssociationWithActionProfile() {
    MappingProfileUpdateDto mappingProfile1 = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_1)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MappingProfileUpdateDto.class);

    ActionProfileUpdateDto actionProfile = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("testActionProfile")
          .withAction(CREATE)
          .withFolioRecord(INSTANCE))
        .withAddedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE)
          .withDetailProfileId(mappingProfile1.getProfile().getId()))))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("profileAssociations.size", is(1))
      .body("profileAssociations[0].masterProfileId", is(actionProfile.getProfile().getId()))
      .body("profileAssociations[0].detailProfileId", is(mappingProfile1.getProfile().getId()));

    MappingProfileUpdateDto mappingProfile2 = RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile().withName("mapping profile 2")
          .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
          .withExistingRecordType(EntityType.INSTANCE))
        .withAddedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE)
          .withMasterProfileId(actionProfile.getProfile().getId()))))
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MappingProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(1))
      .body("profileAssociations.size", is(1))
      .body("profileAssociations[0].masterProfileId", is(actionProfile.getProfile().getId()))
      .body("profileAssociations[0].detailProfileId", is(mappingProfile2.getProfile().getId()));
  }

  @Test
  public void shouldUpdateProfileOnPutAndReplaceExistingAssociationWithActionProfile() {
    MappingProfileUpdateDto mappingProfile1 = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_1)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(MappingProfileUpdateDto.class);

    MappingProfileUpdateDto mappingProfile2 = RestAssured.given()
      .spec(spec)
      .body(mappingProfile_2)
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("profile.name", is("Boo"))
      .extract().body().as(MappingProfileUpdateDto.class);

    ActionProfileUpdateDto actionProfile = RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto()
        .withProfile(new ActionProfile()
          .withName("testActionProfile")
          .withAction(CREATE)
          .withFolioRecord(INSTANCE))
        .withAddedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE)
          .withDetailProfileId(mappingProfile1.getProfile().getId()))))
      .when()
      .post(ACTION_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(ActionProfileUpdateDto.class);

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("profileAssociations.size", is(1))
      .body("profileAssociations[0].masterProfileId", is(actionProfile.getProfile().getId()))
      .body("profileAssociations[0].detailProfileId", is(mappingProfile1.getProfile().getId()));

    mappingProfile2.getProfile().setName("mapping profile 2");
    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile().withName("mapping profile 2")
          .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
          .withExistingRecordType(EntityType.INSTANCE))
        .withAddedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE)
          .withMasterProfileId(actionProfile.getProfile().getId())
          .withDetailProfileId(mappingProfile2.getProfile().getId()))))
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + mappingProfile2.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("name", is("mapping profile 2"));

    RestAssured.given()
      .spec(spec)
      .queryParam("master", ACTION_PROFILE.value())
      .queryParam("detail", MAPPING_PROFILE.value())
      .when()
      .get(ASSOCIATED_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("profileAssociations.size", is(1))
      .body("profileAssociations[0].masterProfileId", is(actionProfile.getProfile().getId()))
      .body("profileAssociations[0].detailProfileId", is(mappingProfile2.getProfile().getId()));
  }


  @Test
  public void shouldNotCreateMappingProfilesWhenDifferentFolioRecord() {
    var actionProfileUpdateDto = postActionProfile(new ActionProfileUpdateDto()
      .withProfile(new ActionProfile()
        .withName("Test Action Profile")
        .withAction(CREATE)
        .withFolioRecord(MARC_BIBLIOGRAPHIC)));

    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile()
          .withName("Test Action Profile")
          .withExistingRecordType(EntityType.INSTANCE)
          .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC))
        .withAddedRelations(List.of(
          new ProfileAssociation()
            .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
            .withMasterProfileId(actionProfileUpdateDto.getProfile().getId())
            .withDetailProfileType(DetailProfileType.MAPPING_PROFILE))))
      .when()
      .post(MAPPING_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Action profile 'Test Action Profile' can not be linked to this Mapping profile. FolioRecord and ExistingRecordType types are different")
        )));
  }

  @Test
  public void shouldNotUpdateMappingProfilesWhenDifferentFolioRecordAndUpdateRecordTypeWhenRecordTypesAreDifferent() {
    var actionProfileUpdateDto = postActionProfile(new ActionProfileUpdateDto()
      .withProfile(new ActionProfile()
        .withName("Test Action Profile")
        .withAction(CREATE)
        .withFolioRecord(INSTANCE)));
    var actionProfileUpdateDto1 = postActionProfile(new ActionProfileUpdateDto()
      .withProfile(new ActionProfile()
        .withName("Test Action Profile1")
        .withAction(UPDATE)
        .withFolioRecord(MARC_BIBLIOGRAPHIC)));

    var mappingProfileUpdateDto = postMappingProfile(new MappingProfileUpdateDto()
      .withProfile(new MappingProfile()
        .withName("Test Mapping Profile")
        .withExistingRecordType(EntityType.INSTANCE)
        .withIncomingRecordType(EntityType.INSTANCE))
      .withAddedRelations(List.of(new ProfileAssociation()
        .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
        .withMasterProfileId(actionProfileUpdateDto.getId())
        .withDetailProfileType(DetailProfileType.MAPPING_PROFILE))));

    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile()
          .withName("Test Mapping Profile")
          .withExistingRecordType(EntityType.INSTANCE)
          .withIncomingRecordType(EntityType.INSTANCE))
        .withAddedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withMasterProfileId(actionProfileUpdateDto1.getProfile().getId())
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE))
        )
        .withDeletedRelations(List.of(new ProfileAssociation()
          .withMasterProfileType(MasterProfileType.ACTION_PROFILE)
          .withMasterProfileId(actionProfileUpdateDto.getProfile().getId())
          .withDetailProfileType(DetailProfileType.MAPPING_PROFILE))))
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + mappingProfileUpdateDto.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Action profile 'Test Action Profile1' can not be linked to this Mapping profile. FolioRecord and ExistingRecordType types are different")
        )));

    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto()
        .withProfile(new MappingProfile()
          .withName("Test Mapping Profile")
          .withExistingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
          .withIncomingRecordType(EntityType.INSTANCE)
          .withParentProfiles(List.of(new ProfileSnapshotWrapper()
            .withId(actionProfileUpdateDto.getProfile().getId())
            .withContent(actionProfileUpdateDto)
            .withContentType(ACTION_PROFILE)))))
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + mappingProfileUpdateDto.getProfile().getId())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
      .body("errors", hasItem(
        hasEntry(is("message"),
          is("Can not update MappingProfile recordType and linked ActionProfile recordType are different")
        )));
  }

  private void createProfiles() {
    List<MappingProfileUpdateDto> mappingProfilesToPost = Arrays.asList(mappingProfile_1, mappingProfile_2, mappingProfile_3);
    for (MappingProfileUpdateDto profile : mappingProfilesToPost) {
      RestAssured.given()
        .spec(spec)
        .body(profile)
        .when()
        .post(MAPPING_PROFILES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient pgClient = PostgresClient.getInstance(vertx, TENANT_ID);
    pgClient.delete(JOB_TO_ACTION_PROFILES_TABLE, new Criterion(), event ->
      pgClient.delete(JOB_TO_MATCH_PROFILES_TABLE, new Criterion(), event2 ->
        pgClient.delete(ACTION_TO_MAPPING_PROFILES_TABLE, new Criterion(), event3 ->
          pgClient.delete(MATCH_TO_MATCH_PROFILES_TABLE, new Criterion(), event4 ->
            pgClient.delete(MATCH_TO_ACTION_PROFILES_TABLE, new Criterion(), event5 ->
              pgClient.delete(JOB_PROFILES_TABLE_NAME, new Criterion(), event6 ->
                pgClient.delete(MATCH_PROFILES_TABLE_NAME, new Criterion(), event7 ->
                  pgClient.delete(ACTION_PROFILES_TABLE_NAME, new Criterion(), event8 ->
                    pgClient.delete(MAPPING_PROFILES_TABLE_NAME, new Criterion(), event9 -> {
                      if (event7.failed()) {
                        context.fail(event7.cause());
                      }
                      async.complete();
                    })))))))));
  }

}
