package org.folio.unit;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.folio.dao.PostgresClientFactory;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.dao.snapshot.ProfileSnapshotDaoImpl;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.services.snapshot.ProfileSnapshotService;
import org.folio.services.snapshot.ProfileSnapshotServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(VertxUnitRunner.class)
public class ProfileSnapshotServiceTest {

  private static final String TENANT_ID = "diku";
  private static final String TABLE_NAME = "profile_snapshots";
  private Vertx vertx = Vertx.vertx();
  private PostgresClientFactory pgClientFactoryMock = Mockito.mock(PostgresClientFactory.class);
  private ProfileSnapshotDao dao = new ProfileSnapshotDaoImpl(pgClientFactoryMock);
  private ProfileSnapshotService service = new ProfileSnapshotServiceImpl(dao);

  public ProfileSnapshotServiceTest() {
    MockitoAnnotations.initMocks(pgClientFactoryMock);
    Mockito.when(pgClientFactoryMock.createInstance(TENANT_ID)).thenReturn(PostgresClient.getInstance(vertx, TENANT_ID));
  }

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
        assertTrue(optionalAr.isPresent());

        ProfileSnapshotWrapper actualJobProfileWrapper = optionalAr.get();
        assertEquals(expectedJobProfileWrapper.getId(), actualJobProfileWrapper.getId());
        assertEquals(expectedJobProfileWrapper.getContentType(), actualJobProfileWrapper.getContentType());
        assertEquals(expectedJobProfileWrapper.getContent().getClass(), actualJobProfileWrapper.getContent().getClass());

        ChildSnapshotWrapper expectedMatchProfileWrapper = expectedJobProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualMatchProfileWrapper = actualJobProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMatchProfileWrapper, actualMatchProfileWrapper);

        ChildSnapshotWrapper expectedActionProfileWrapper = expectedMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualActionProfileWrapper = actualMatchProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedActionProfileWrapper, actualActionProfileWrapper);

        ChildSnapshotWrapper expectedMappingProfileWrapper = expectedActionProfileWrapper.getChildSnapshotWrappers().get(0);
        ChildSnapshotWrapper actualMappingProfileWrapper = actualActionProfileWrapper.getChildSnapshotWrappers().get(0);
        assertExpectedChildOnActualChild(expectedMappingProfileWrapper, actualMappingProfileWrapper);

        async.complete();
        return Future.future();
      });
      return Future.future();
    });
  }

  private void assertExpectedChildOnActualChild(ChildSnapshotWrapper expected, ChildSnapshotWrapper actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getContentType(), actual.getContentType());
    assertEquals(expected.getContent().getClass(), actual.getContent().getClass());
  }

  @After
  public void afterTest(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      }
      PostgresClient.closeAllClients();
      async.complete();
    });
  }
}
