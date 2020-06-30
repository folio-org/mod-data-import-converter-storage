package org.folio.unit.snapshot;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.dao.snapshot.ProfileSnapshotDaoImpl;
import org.folio.dao.snapshot.ProfileSnapshotItem;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.services.snapshot.ProfileSnapshotService;
import org.folio.services.snapshot.ProfileSnapshotServiceImpl;
import org.folio.unit.AbstractUnitTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;

public class ProfileSnapshotServiceTest extends AbstractUnitTest {
  private static final String TABLE_NAME = "profile_snapshots";

  @Autowired
  private ProfileSnapshotDao dao;
  @Autowired
  private ProfileSnapshotService service;

  private JobProfile jobProfile = new JobProfile().withId(UUID.randomUUID().toString());
  private ProfileSnapshotItem jobProfileSnapshotItem = new ProfileSnapshotItem();

  private MatchProfile matchProfile = new MatchProfile().withId(UUID.randomUUID().toString());
  private ProfileSnapshotItem matchProfileSnapshotItem = new ProfileSnapshotItem();

  private ActionProfile actionProfile = new ActionProfile().withId(UUID.randomUUID().toString());
  private ProfileSnapshotItem actionProfileSnapshotItem = new ProfileSnapshotItem();

  private MappingProfile mappingProfile = new MappingProfile().withId(UUID.randomUUID().toString());
  private ProfileSnapshotItem mappingProfileSnapshotItem = new ProfileSnapshotItem();

  private List<ProfileSnapshotItem> items;

  @Before
  public void setUp() {
    jobProfileSnapshotItem.setAssociationId(UUID.randomUUID().toString());
    jobProfileSnapshotItem.setMasterId(null);
    jobProfileSnapshotItem.setDetailId(jobProfile.getId());
    jobProfileSnapshotItem.setDetailType(JOB_PROFILE);
    jobProfileSnapshotItem.setDetail(jobProfile);

    matchProfileSnapshotItem.setAssociationId(UUID.randomUUID().toString());
    matchProfileSnapshotItem.setMasterId(jobProfile.getId());
    matchProfileSnapshotItem.setDetailId(matchProfile.getId());
    matchProfileSnapshotItem.setDetailType(MATCH_PROFILE);
    matchProfileSnapshotItem.setDetail(matchProfile);

    actionProfileSnapshotItem.setAssociationId(UUID.randomUUID().toString());
    actionProfileSnapshotItem.setMasterId(matchProfile.getId());
    actionProfileSnapshotItem.setDetailId(actionProfile.getId());
    actionProfileSnapshotItem.setDetailType(ACTION_PROFILE);
    actionProfileSnapshotItem.setDetail(actionProfile);

    mappingProfileSnapshotItem.setAssociationId(UUID.randomUUID().toString());
    mappingProfileSnapshotItem.setMasterId(actionProfile.getId());
    mappingProfileSnapshotItem.setDetailId(mappingProfile.getId());
    mappingProfileSnapshotItem.setDetailType(MAPPING_PROFILE);
    mappingProfileSnapshotItem.setDetail(mappingProfile);

    items = new ArrayList<>(Arrays.asList(
      jobProfileSnapshotItem,
      actionProfileSnapshotItem,
      matchProfileSnapshotItem,
      mappingProfileSnapshotItem)
    );
  }

  @Test
  public void shouldSaveAndReturnWrappersOnGetById(TestContext context) {
    Async async = context.async();

    ProfileSnapshotWrapper expectedJobProfileWrapper = new ProfileSnapshotWrapper()
      .withId(UUID.randomUUID().toString())
      .withContentType(JOB_PROFILE)
      .withContent(new JobProfile())
      .withChildSnapshotWrappers(Collections.singletonList(
        new ProfileSnapshotWrapper()
          .withId(UUID.randomUUID().toString())
          .withContentType(ProfileSnapshotWrapper.ContentType.MATCH_PROFILE)
          .withContent(new MatchProfile())
          .withReactTo(ProfileSnapshotWrapper.ReactTo.MATCH)
          .withOrder(1)
          .withChildSnapshotWrappers(Collections.singletonList(
            new ProfileSnapshotWrapper()
              .withId(UUID.randomUUID().toString())
              .withContentType(ProfileSnapshotWrapper.ContentType.ACTION_PROFILE)
              .withContent(new ActionProfile())
              .withReactTo(ProfileSnapshotWrapper.ReactTo.MATCH)
              .withOrder(1)
              .withChildSnapshotWrappers(Collections.singletonList(
                new ProfileSnapshotWrapper()
                  .withId(UUID.randomUUID().toString())
                  .withReactTo(ProfileSnapshotWrapper.ReactTo.MATCH)
                  .withOrder(1)
                  .withContentType(ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE)
                  .withContent(new MappingProfile())
              ))
          ))
      ));

    dao.save(expectedJobProfileWrapper, TENANT_ID).compose(ar -> {
      service.getById(expectedJobProfileWrapper.getId(), TENANT_ID).compose(optionalAr -> {
        context.assertTrue(optionalAr.isPresent());

        ProfileSnapshotWrapper actualJobProfileWrapper = optionalAr.get();
        context.assertEquals(expectedJobProfileWrapper.getId(), actualJobProfileWrapper.getId());
        context.assertEquals(expectedJobProfileWrapper.getContentType(), actualJobProfileWrapper.getContentType());
        context.assertEquals(expectedJobProfileWrapper.getContent().getClass(), actualJobProfileWrapper.getContent().getClass());

        ProfileSnapshotWrapper expectedMatchProfileWrapper = expectedJobProfileWrapper.getChildSnapshotWrappers().get(0);
        ProfileSnapshotWrapper actualMatchProfileWrapper = actualJobProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMatchProfileWrapper, actualMatchProfileWrapper, context);

        ProfileSnapshotWrapper expectedActionProfileWrapper = expectedMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        ProfileSnapshotWrapper actualActionProfileWrapper = actualMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedActionProfileWrapper, actualActionProfileWrapper, context);

        ProfileSnapshotWrapper expectedMappingProfileWrapper = expectedActionProfileWrapper.getChildSnapshotWrappers().get(0);
        ProfileSnapshotWrapper actualMappingProfileWrapper = actualActionProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMappingProfileWrapper, actualMappingProfileWrapper, context);

        async.complete();
        return Future.succeededFuture();
      });
      return Future.succeededFuture();
    });
  }

  @Test
  public void shouldReturnFailedFutureIfNoSnapshotItemsExist(TestContext context) {
    Async async = context.async();
    // given
    ProfileSnapshotDao mockDao = Mockito.mock(ProfileSnapshotDaoImpl.class);
    ProfileSnapshotService service = new ProfileSnapshotServiceImpl(dao);

    String jobProfileId = UUID.randomUUID().toString();
    Mockito.when(mockDao.getSnapshotItems(jobProfileId, JOB_PROFILE, jobProfileId, TENANT_ID)).thenReturn(Future.succeededFuture(new ArrayList<>()));

    // when
    service.createSnapshot(jobProfileId, TENANT_ID).onComplete(ar -> {
    // then
      context.assertTrue(ar.failed());
      async.complete();
    });
  }

  @Test
  public void shouldBuildAndSaveSnapshotForJobProfile(TestContext testContext) {
    Async async = testContext.async();
    // given
    ProfileSnapshotDao mockDao = Mockito.mock(ProfileSnapshotDaoImpl.class);
    ProfileSnapshotService service = new ProfileSnapshotServiceImpl(mockDao);

    Mockito.when(mockDao.getSnapshotItems(jobProfile.getId(), JOB_PROFILE, jobProfile.getId(), TENANT_ID)).thenReturn(Future.succeededFuture(items));
    Mockito.when(mockDao.save(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Future.succeededFuture(jobProfile.getId()));

    // when
    service.createSnapshot(jobProfile.getId(), TENANT_ID).onComplete(ar -> {
    // then
      testContext.assertTrue(ar.succeeded());
      ProfileSnapshotWrapper jobProfileWrapper = ar.result();
      JobProfile actualJobProfile = (JobProfile) jobProfileWrapper.getContent();
      testContext.assertEquals(jobProfile.getId(), actualJobProfile.getId());
      testContext.assertEquals(jobProfile.getId(), jobProfileWrapper.getProfileId());

      ProfileSnapshotWrapper matchProfileWrapper = jobProfileWrapper.getChildSnapshotWrappers().get(0);
      MatchProfile actualMatchProfile = (MatchProfile) matchProfileWrapper.getContent();
      testContext.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
      testContext.assertEquals(matchProfile.getId(), matchProfileWrapper.getProfileId());

      ProfileSnapshotWrapper actionProfileWrapper = matchProfileWrapper.getChildSnapshotWrappers().get(0);
      ActionProfile actualActionProfile = (ActionProfile) actionProfileWrapper.getContent();
      testContext.assertEquals(actionProfile.getId(), actualActionProfile.getId());
      testContext.assertEquals(actionProfile.getId(), actionProfileWrapper.getProfileId());

      ProfileSnapshotWrapper mappingProfileWrapper = actionProfileWrapper.getChildSnapshotWrappers().get(0);
      MappingProfile actualMappingProfile = (MappingProfile) mappingProfileWrapper.getContent();
      testContext.assertEquals(mappingProfile.getId(), actualMappingProfile.getId());
      testContext.assertEquals(mappingProfile.getId(), mappingProfileWrapper.getProfileId());
      async.complete();
    });
  }

  @Test
  public void shouldConstructSnapshotForJobProfile(TestContext testContext) {
    Async async = testContext.async();
    // given
    ProfileSnapshotDao mockDao = Mockito.mock(ProfileSnapshotDaoImpl.class);
    ProfileSnapshotService service = new ProfileSnapshotServiceImpl(mockDao);

    Mockito.when(mockDao.getSnapshotItems(jobProfile.getId(), JOB_PROFILE, jobProfile.getId(), TENANT_ID)).thenReturn(Future.succeededFuture(items));

    // when
    service.constructSnapshot(jobProfile.getId(), JOB_PROFILE, jobProfile.getId(), TENANT_ID).onComplete(ar -> {
    // then
      testContext.assertTrue(ar.succeeded());
      ProfileSnapshotWrapper jobProfileWrapper = ar.result();
      JobProfile actualJobProfile = (JobProfile) jobProfileWrapper.getContent();
      testContext.assertEquals(jobProfile.getId(), actualJobProfile.getId());
      testContext.assertEquals(jobProfile.getId(), jobProfileWrapper.getProfileId());

      ProfileSnapshotWrapper matchProfileWrapper = jobProfileWrapper.getChildSnapshotWrappers().get(0);
      MatchProfile actualMatchProfile = (MatchProfile) matchProfileWrapper.getContent();
      testContext.assertEquals(matchProfile.getId(), actualMatchProfile.getId());
      testContext.assertEquals(matchProfile.getId(), matchProfileWrapper.getProfileId());

      ProfileSnapshotWrapper actionProfileWrapper = matchProfileWrapper.getChildSnapshotWrappers().get(0);
      ActionProfile actualActionProfile = (ActionProfile) actionProfileWrapper.getContent();
      testContext.assertEquals(actionProfile.getId(), actualActionProfile.getId());
      testContext.assertEquals(actionProfile.getId(), actionProfileWrapper.getProfileId());

      ProfileSnapshotWrapper mappingProfileWrapper = actionProfileWrapper.getChildSnapshotWrappers().get(0);
      MappingProfile actualMappingProfile = (MappingProfile) mappingProfileWrapper.getContent();
      testContext.assertEquals(mappingProfile.getId(), actualMappingProfile.getId());
      testContext.assertEquals(mappingProfile.getId(), mappingProfileWrapper.getProfileId());
      async.complete();
    });
  }

  private void assertExpectedChildOnActualChild(ProfileSnapshotWrapper expected, ProfileSnapshotWrapper actual, TestContext context) {
    context.assertEquals(expected.getId(), actual.getId());
    context.assertEquals(expected.getContentType(), actual.getContentType());
    context.assertEquals(expected.getContent().getClass(), actual.getContent().getClass());
  }

  @After
  public void afterTest(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      }
      async.complete();
    });
  }
}
