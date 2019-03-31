package org.folio.unit.snapshot;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.services.snapshot.ProfileSnapshotService;
import org.folio.unit.AbstractUnitTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.UUID;

public class ProfileSnapshotServiceTest extends AbstractUnitTest {
  private static final String TABLE_NAME = "profile_snapshots";
  @Autowired
  private ProfileSnapshotDao dao;
  @Autowired
  private ProfileSnapshotService service;

  @Test
  public void shouldConvertWrappersByContentType(TestContext context) {
    Async async = context.async();

    ProfileSnapshotWrapper expectedJobProfileWrapper = new ProfileSnapshotWrapper()
      .withId(UUID.randomUUID().toString())
      .withContentType(ProfileSnapshotWrapper.ContentType.JOB_PROFILE)
      .withContent(new JobProfile())
      .withChildSnapshotWrappers(Collections.singletonList(
        new ChildSnapshotWrapper()
          .withId(UUID.randomUUID().toString())
          .withContentType(ProfileSnapshotWrapper.ContentType.MATCH_PROFILE)
          .withContent(new MatchProfile())
          .withChildSnapshotWrappers(Collections.singletonList(
            new ChildSnapshotWrapper()
              .withId(UUID.randomUUID().toString())
              .withContentType(ProfileSnapshotWrapper.ContentType.ACTION_PROFILE)
              .withContent(new ActionProfile())
              .withChildSnapshotWrappers(Collections.singletonList(
                new ChildSnapshotWrapper()
                  .withId(UUID.randomUUID().toString())
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

        ChildSnapshotWrapper expectedMatchProfileWrapper = expectedJobProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualMatchProfileWrapper = actualJobProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMatchProfileWrapper, actualMatchProfileWrapper, context);

        ChildSnapshotWrapper expectedActionProfileWrapper = expectedMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualActionProfileWrapper = actualMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedActionProfileWrapper, actualActionProfileWrapper, context);

        ChildSnapshotWrapper expectedMappingProfileWrapper = expectedActionProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualMappingProfileWrapper = actualActionProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMappingProfileWrapper, actualMappingProfileWrapper, context);

        async.complete();
        return Future.succeededFuture();
      });
      return Future.succeededFuture();
    });
  }

  private void assertExpectedChildOnActualChild(ChildSnapshotWrapper expected, ChildSnapshotWrapper actual, TestContext context) {
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
