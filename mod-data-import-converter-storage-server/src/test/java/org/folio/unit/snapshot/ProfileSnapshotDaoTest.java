package org.folio.unit.snapshot;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import org.folio.dao.ProfileDao;
import org.folio.dao.association.ProfileAssociationDao;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.dao.snapshot.ProfileSnapshotItem;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.unit.AbstractUnitTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;

public class ProfileSnapshotDaoTest extends AbstractUnitTest {

  private static final String SNAPSHOTS_TABLE_NAME = "profile_snapshots";
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String JOB_TO_MATCH_PROFILES_TABLE_NAME = "job_to_match_profiles";
  private static final String MATCH_TO_ACTION_PROFILES_TABLE_NAME = "match_to_action_profiles";
  private static final String ACTION_TO_MAPPING_PROFILES_TABLE_NAME = "action_to_mapping_profiles";
  private static final String JOB_TO_ACTION_PROFILES_TABLE_NAME = "job_to_action_profiles";
  private static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";
  private static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  @Autowired
  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;
  @Autowired
  private ProfileDao<ActionProfile, ActionProfileCollection> actionProfileDao;
  @Autowired
  private ProfileAssociationDao profileAssociationDao;
  @Autowired
  private ProfileSnapshotDao dao;

  @Test
  public void shouldReturnEmptySnapshotItemsIfNoItemsExist(TestContext context) {
    String jobProfileId = UUID.randomUUID().toString();
    dao.getSnapshotItems(jobProfileId, JOB_PROFILE, jobProfileId, TENANT_ID).onComplete(ar -> {
      context.assertTrue(ar.succeeded());
      List<ProfileSnapshotItem> items = ar.result();
      context.assertTrue(items.isEmpty());
    });
  }

  @Test
  public void shouldReturn2SnapshotItems(TestContext context) {
    Async async = context.async();
    // given
    JobProfile jobProfile = new JobProfile().withId(UUID.randomUUID().toString());
    ActionProfile actionProfile = new ActionProfile().withId(UUID.randomUUID().toString());
    ProfileAssociation jobToAction1Association = new ProfileAssociation()
      .withId(UUID.randomUUID().toString())
      .withOrder(0)
      .withReactTo(ProfileAssociation.ReactTo.MATCH)
      .withOrder(1)
      .withMasterProfileId(jobProfile.getId())
      .withDetailProfileId(actionProfile.getId());
    // when
    jobProfileDao.saveProfile(jobProfile, TENANT_ID).onComplete(savedJobProfileAr -> {
      context.assertTrue(savedJobProfileAr.succeeded());
      actionProfileDao.saveProfile(actionProfile, TENANT_ID).onComplete(savedActionProfileAr -> {
        context.assertTrue(savedActionProfileAr.succeeded());
        profileAssociationDao.save(jobToAction1Association, JOB_PROFILE, ACTION_PROFILE, TENANT_ID).onComplete(savedAssociationAr -> {
          context.assertTrue(savedAssociationAr.succeeded());
          dao.getSnapshotItems(jobProfile.getId(), JOB_PROFILE, jobProfile.getId(), TENANT_ID).onComplete(itemsAr -> {
            // then
            context.assertTrue(itemsAr.succeeded());
            List<ProfileSnapshotItem> profileSnapshotItems = itemsAr.result();
            context.assertEquals(2, profileSnapshotItems.size());
            async.complete();
          });
        });
      });
    });
  }

  @After
  public void afterTest(TestContext context) {
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
                      pgClient.delete(MAPPING_PROFILES_TABLE_NAME, new Criterion(), event10 -> {
                        if (event10.failed()) {
                          context.fail(event10.cause());
                        }
                        async.complete();
                      })))))))));
  }
}
