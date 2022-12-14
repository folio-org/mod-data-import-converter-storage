package org.folio.rest.impl.snapshot;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.apache.http.HttpStatus;
import org.folio.rest.impl.AbstractRestVerticleTest;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.EntityType;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileUpdateDto;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.folio.rest.jaxrs.model.ActionProfile.Action.CREATE;
import static org.folio.rest.jaxrs.model.ActionProfile.FolioRecord.MARC_BIBLIOGRAPHIC;
import static org.folio.rest.jaxrs.model.JobProfile.DataType.MARC;
import static org.folio.rest.jaxrs.model.ProfileAssociation.ReactTo.MATCH;
import static org.folio.rest.jaxrs.model.ProfileAssociation.ReactTo.NON_MATCH;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;

@RunWith(VertxUnitRunner.class)
public class JobProfileSnapshotTest extends AbstractRestVerticleTest {

  private static final String JOB_PROFILE_SNAPSHOT_PATH = "/data-import-profiles/jobProfileSnapshots";
  private static final String PROFILE_SNAPSHOT_PATH = "/data-import-profiles/profileSnapshots";
  private static final String JOB_PROFILES_PATH = "/data-import-profiles/jobProfiles";
  private static final String ACTION_PROFILES_PATH = "/data-import-profiles/actionProfiles";
  private static final String MATCH_PROFILES_PATH = "/data-import-profiles/matchProfiles";
  private static final String MAPPING_PROFILES_PATH = "/data-import-profiles/mappingProfiles";
  private static final String PROFILE_TYPE_PARAM = "profileType";
  private static final String JOB_PROFILE_ID_PARAM = "jobProfileId";

  private static final String JOB_TO_MATCH_PROFILES_TABLE_NAME = "job_to_match_profiles";
  private static final String MATCH_TO_ACTION_PROFILES_TABLE_NAME = "match_to_action_profiles";
  private static final String ACTION_TO_MAPPING_PROFILES_TABLE_NAME = "action_to_mapping_profiles";
  private static final String JOB_TO_ACTION_PROFILES_TABLE_NAME = "job_to_action_profiles";
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";
  private static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String SNAPSHOTS_TABLE_NAME = "profile_snapshots";
  private static final String ACTION_TO_ACTION_PROFILES_TABLE_NAME = "action_to_action_profiles";
  private static final String MATCH_TO_MATCH_PROFILES_TABLE_NAME = "match_to_match_profiles";


  private JobProfileUpdateDto jobProfile = new JobProfileUpdateDto()
    .withProfile(new JobProfile().withName("testJobProfile1").withDataType(MARC).withDescription("test-description"));

  private MatchProfileUpdateDto matchProfile = new MatchProfileUpdateDto()
    .withProfile(new MatchProfile().withName("testMatchProfile1")
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withDescription("test-description"));

  private ActionProfileUpdateDto actionProfile = new ActionProfileUpdateDto()
    .withProfile(new ActionProfile().withName("testActionProfile1").withDescription("test-description")
      .withAction(CREATE).withFolioRecord(MARC_BIBLIOGRAPHIC));

  private MappingProfileUpdateDto mappingProfile = new MappingProfileUpdateDto()
    .withProfile(new MappingProfile().withName("testMappingProfile1").withDescription("test-description")
      .withIncomingRecordType(EntityType.MARC_BIBLIOGRAPHIC)
      .withExistingRecordType(EntityType.MARC_BIBLIOGRAPHIC));

  @Before
  public void setUp(TestContext testContext) {
    super.setUp(testContext);
    jobProfile = postProfile(testContext, jobProfile, JOB_PROFILES_PATH).body().as(JobProfileUpdateDto.class);
    matchProfile = postProfile(testContext, matchProfile, MATCH_PROFILES_PATH).body().as(MatchProfileUpdateDto.class);
    actionProfile = postProfile(testContext, actionProfile, ACTION_PROFILES_PATH).body().as(ActionProfileUpdateDto.class);
    mappingProfile = postProfile(testContext, mappingProfile, MAPPING_PROFILES_PATH).body().as(MappingProfileUpdateDto.class);

    jobProfile.withAddedRelations(Collections.singletonList(new ProfileAssociation()
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(matchProfile.getId())
      .withMasterProfileType(ProfileAssociation.MasterProfileType.JOB_PROFILE)
      .withDetailProfileType(ProfileAssociation.DetailProfileType.MATCH_PROFILE)
      .withOrder(0)));
    matchProfile.withAddedRelations(Collections.singletonList(new ProfileAssociation()
      .withMasterProfileId(matchProfile.getId())
      .withDetailProfileId(actionProfile.getId())
      .withMasterProfileType(ProfileAssociation.MasterProfileType.MATCH_PROFILE)
      .withDetailProfileType(ProfileAssociation.DetailProfileType.ACTION_PROFILE)
      .withReactTo(NON_MATCH)
      .withJobProfileId(jobProfile.getId())
      .withOrder(0)));
    actionProfile.withAddedRelations(Collections.singletonList(new ProfileAssociation()
      .withMasterProfileId(actionProfile.getId())
      .withDetailProfileId(mappingProfile.getId())
      .withMasterProfileType(ProfileAssociation.MasterProfileType.ACTION_PROFILE)
      .withDetailProfileType(ProfileAssociation.DetailProfileType.MAPPING_PROFILE)
      .withOrder(0)));

    updateProfile(testContext, jobProfile, jobProfile.getId(), JOB_PROFILES_PATH);
    updateProfile(testContext, matchProfile, matchProfile.getId(), MATCH_PROFILES_PATH);
    updateProfile(testContext, actionProfile, actionProfile.getId(), ACTION_PROFILES_PATH);
    updateProfile(testContext, mappingProfile, mappingProfile.getId(), MAPPING_PROFILES_PATH);
  }

  @Test
  public void shouldReturnNotFoundOnGetById(TestContext testContext) {
    Async async = testContext.async();
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILE_SNAPSHOT_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void shouldBuildAndReturn500OnGetById(TestContext testContext) {
    Async async = testContext.async();
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .post(JOB_PROFILE_SNAPSHOT_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    async.complete();
  }

  @Test
  public void shouldBuildAndReturn500OnGetSnapshotById(TestContext testContext) {
    Async async = testContext.async();
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, JOB_PROFILE.value())
      .get(PROFILE_SNAPSHOT_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    async.complete();
  }

  @Test
  public void shouldReturnBadRequestWhenProfileTypeQueryParamIsMissed(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(PROFILE_SNAPSHOT_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
    async.complete();
  }

  @Test
  public void shouldReturnBadRequestWhenProfileTypeQueryParamIsInvalid(TestContext testContext) {
    Async async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, "invalid param")
      .get(PROFILE_SNAPSHOT_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
    async.complete();
  }

  @Test
  public void shouldReturnSnapshotWrapperOnGetByProfileIdForJobProfile(TestContext testContext) {
    Async async = testContext.async();
    ProfileSnapshotWrapper jobProfileSnapshot = RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, JOB_PROFILE.value())
      .queryParam(JOB_PROFILE_ID_PARAM, jobProfile.getId())
      .get(PROFILE_SNAPSHOT_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(ProfileSnapshotWrapper.class);

    JobProfile actualJobProfile = DatabindCodec.mapper().convertValue(jobProfileSnapshot.getContent(), JobProfile.class);
    Assert.assertEquals(jobProfile.getId(), actualJobProfile.getId());
    Assert.assertEquals(1, jobProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper matchProfileSnapshot = jobProfileSnapshot.getChildSnapshotWrappers().get(0);
    MatchProfile actualMatchProfile = DatabindCodec.mapper().convertValue(matchProfileSnapshot.getContent(), MatchProfile.class);
    Assert.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
    Assert.assertEquals(1, matchProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper actionProfileSnapshot = matchProfileSnapshot.getChildSnapshotWrappers().get(0);
    ActionProfile actualActionProfile = DatabindCodec.mapper().convertValue(actionProfileSnapshot.getContent(), ActionProfile.class);
    Assert.assertEquals(actionProfile.getId(), actualActionProfile.getId());
    Assert.assertEquals(1, actionProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper mappingProfileSnapshot = actionProfileSnapshot.getChildSnapshotWrappers().get(0);
    MappingProfile mappingActionProfile = DatabindCodec.mapper().convertValue(mappingProfileSnapshot.getContent(), MappingProfile.class);
    Assert.assertEquals(mappingProfile.getId(), mappingActionProfile.getId());
    Assert.assertEquals(0, mappingProfileSnapshot.getChildSnapshotWrappers().size());
    async.complete();
  }

  @Test
  public void shouldReturnSnapshotWrapperOnGetByProfileIdForMatchProfile(TestContext testContext) {
    Async async = testContext.async();
    ProfileSnapshotWrapper matchProfileSnapshot = RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, MATCH_PROFILE.value())
      .queryParam(JOB_PROFILE_ID_PARAM, jobProfile.getId())
      .get(PROFILE_SNAPSHOT_PATH + "/" + matchProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(ProfileSnapshotWrapper.class);

    MatchProfile actualMatchProfile = DatabindCodec.mapper().convertValue(matchProfileSnapshot.getContent(), MatchProfile.class);
    Assert.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
    Assert.assertEquals(1, matchProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper actionProfileSnapshot = matchProfileSnapshot.getChildSnapshotWrappers().get(0);
    Assert.assertEquals(ProfileSnapshotWrapper.ReactTo.NON_MATCH, actionProfileSnapshot.getReactTo());
    ActionProfile actualActionProfile = DatabindCodec.mapper().convertValue(actionProfileSnapshot.getContent(), ActionProfile.class);
    Assert.assertEquals(actionProfile.getId(), actualActionProfile.getId());
    Assert.assertEquals(1, actionProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper mappingProfileSnapshot = actionProfileSnapshot.getChildSnapshotWrappers().get(0);
    MappingProfile mappingActionProfile = DatabindCodec.mapper().convertValue(mappingProfileSnapshot.getContent(), MappingProfile.class);
    Assert.assertEquals(mappingProfile.getId(), mappingActionProfile.getId());
    Assert.assertEquals(0, mappingProfileSnapshot.getChildSnapshotWrappers().size());
    async.complete();
  }

  @Test
  public void shouldReturnSnapshotWrapperOnGetByProfileIdForActionProfile(TestContext testContext) {
    Async async = testContext.async();
    ProfileSnapshotWrapper actionProfileSnapshot = RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, ACTION_PROFILE.value())
      .get(PROFILE_SNAPSHOT_PATH + "/" + actionProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(ProfileSnapshotWrapper.class);

    ProfileSnapshotWrapper mappingProfileSnapshot = actionProfileSnapshot.getChildSnapshotWrappers().get(0);
    MappingProfile mappingActionProfile = DatabindCodec.mapper().convertValue(mappingProfileSnapshot.getContent(), MappingProfile.class);
    Assert.assertEquals(mappingProfile.getId(), mappingActionProfile.getId());
    Assert.assertEquals(0, mappingProfileSnapshot.getChildSnapshotWrappers().size());
    async.complete();
  }

  @Test
  public void shouldReturnSnapshotWrapperOnGetByProfileIdForJobProfile2(TestContext testContext) {
    Async async = testContext.async();

    ActionProfileUpdateDto actionProfile2 = new ActionProfileUpdateDto()
      .withProfile(new ActionProfile().withName("testActionProfile2").withDescription("test-description")
        .withAction(CREATE).withFolioRecord(MARC_BIBLIOGRAPHIC));

    actionProfile2 = postProfile(testContext, actionProfile2, ACTION_PROFILES_PATH).body().as(ActionProfileUpdateDto.class);

    JobProfileUpdateDto jobProfile2 = new JobProfileUpdateDto()
      .withProfile(new JobProfile().withName("testJobProfile2").withDataType(MARC).withDescription("test-description"))
      .withAddedRelations(Arrays.asList(
        new ProfileAssociation()
          .withDetailProfileId(matchProfile.getId())
          .withMasterProfileType(ProfileAssociation.MasterProfileType.JOB_PROFILE)
          .withDetailProfileType(ProfileAssociation.DetailProfileType.MATCH_PROFILE)
          .withOrder(0),
        new ProfileAssociation()
          .withMasterProfileId(matchProfile.getId())
          .withDetailProfileId(actionProfile2.getId())
          .withMasterProfileType(ProfileAssociation.MasterProfileType.MATCH_PROFILE)
          .withDetailProfileType(ProfileAssociation.DetailProfileType.ACTION_PROFILE)
          .withReactTo(MATCH)
          .withOrder(0)));

    jobProfile2 = postProfile(testContext, jobProfile2, JOB_PROFILES_PATH).body().as(JobProfileUpdateDto.class);

    ProfileSnapshotWrapper jobProfileSnapshot = RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, JOB_PROFILE.value())
      .queryParam(JOB_PROFILE_ID_PARAM, jobProfile2.getId())
      .get(PROFILE_SNAPSHOT_PATH + "/" + jobProfile2.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(ProfileSnapshotWrapper.class);

    JobProfile actualJobProfile = DatabindCodec.mapper().convertValue(jobProfileSnapshot.getContent(), JobProfile.class);
    Assert.assertEquals(jobProfile2.getId(), actualJobProfile.getId());
    Assert.assertEquals(1, jobProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper matchProfileSnapshot = jobProfileSnapshot.getChildSnapshotWrappers().get(0);
    MatchProfile actualMatchProfile = DatabindCodec.mapper().convertValue(matchProfileSnapshot.getContent(), MatchProfile.class);
    Assert.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
    Assert.assertEquals(1, matchProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper actionProfileSnapshot = matchProfileSnapshot.getChildSnapshotWrappers().get(0);
    ActionProfile actualActionProfile = DatabindCodec.mapper().convertValue(actionProfileSnapshot.getContent(), ActionProfile.class);
    Assert.assertEquals(actionProfile2.getId(), actualActionProfile.getId());
    Assert.assertEquals(0, actionProfileSnapshot.getChildSnapshotWrappers().size());
    async.complete();
  }

  @Test
  public void shouldReturnSnapshotWrapperForJobProfileWithoutMatchProfileChildWrappersWhenJobProfileIdParamIsMissed(TestContext testContext) {
    Async async = testContext.async();
    ProfileSnapshotWrapper jobProfileSnapshot = RestAssured.given()
      .spec(spec)
      .when()
      .queryParam(PROFILE_TYPE_PARAM, JOB_PROFILE.value())
      .get(PROFILE_SNAPSHOT_PATH + "/" + jobProfile.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(ProfileSnapshotWrapper.class);

    JobProfile actualJobProfile = DatabindCodec.mapper().convertValue(jobProfileSnapshot.getContent(), JobProfile.class);
    Assert.assertEquals(jobProfile.getId(), actualJobProfile.getId());
    Assert.assertEquals(1, jobProfileSnapshot.getChildSnapshotWrappers().size());

    ProfileSnapshotWrapper matchProfileSnapshot = jobProfileSnapshot.getChildSnapshotWrappers().get(0);
    MatchProfile actualMatchProfile = DatabindCodec.mapper().convertValue(matchProfileSnapshot.getContent(), MatchProfile.class);
    Assert.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
    Assert.assertEquals(0, matchProfileSnapshot.getChildSnapshotWrappers().size());
    async.complete();
  }

  private <T> ExtractableResponse<Response> postProfile(TestContext testContext, T profileDto, String profileUrl) {
    Async async = testContext.async();
    ExtractableResponse<Response> createdProfile = RestAssured.given()
      .spec(spec)
      .body(profileDto)
      .when()
      .post(profileUrl)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract();
    async.complete();
    return createdProfile;
  }

  private <T> ExtractableResponse<Response> updateProfile(TestContext testContext, T profileDto, String profileId, String profileUrl) {
    Async async = testContext.async();
    ExtractableResponse<Response> createdProfile = RestAssured.given()
      .spec(spec)
      .body(profileDto)
      .when()
      .put(profileUrl + "/" + profileId)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract();
    async.complete();
    return createdProfile;
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
