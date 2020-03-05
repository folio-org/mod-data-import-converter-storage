package org.folio.dao.snapshot;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import org.apache.commons.lang3.StringUtils;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation for Profile snapshot DAO
 */
@Repository
@SuppressWarnings("squid:CallToDeprecatedMethod")
public class ProfileSnapshotDaoImpl implements ProfileSnapshotDao {
  private static final Logger logger = LoggerFactory.getLogger(ProfileSnapshotDaoImpl.class);
  private static final String TABLE_NAME = "profile_snapshots";
  private static final String GET_JOB_PROFILE_SNAPSHOT = "select get_job_profile_snapshot('%s');";
  protected PostgresClientFactory pgClientFactory;

  public ProfileSnapshotDaoImpl(@Autowired PostgresClientFactory pgClientFactory) {
    this.pgClientFactory = pgClientFactory;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId) {
    Future<ProfileSnapshotWrapper> future = Future.future();
    try {
      pgClientFactory.createInstance(tenantId).getById(TABLE_NAME, id, ProfileSnapshotWrapper.class, future);
    } catch (Exception e) {
      logger.error("Error querying {} by id", ProfileSnapshotWrapper.class.getSimpleName(), e);
      future.fail(e);
    }
    return future
      .map(wrapper -> wrapper == null ? Optional.empty() : Optional.of(wrapper));
  }

  @Override
  public Future<String> save(ProfileSnapshotWrapper entity, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(TABLE_NAME, entity.getId(), entity, future.completer());
    return future;
  }

  public Future<List<ProfileSnapshotItem>> getSnapshotItems(String jobProfileId, String tenantId) {
    Future<ResultSet> future = Future.future();
    try {
      String createSnapshotQuery = String.format(GET_JOB_PROFILE_SNAPSHOT, jobProfileId);
      pgClientFactory.createInstance(tenantId).select(createSnapshotQuery, future.completer());
    } catch (Exception e) {
      future.fail(e);
    }
    return future
      .map(results -> results.getResults().stream()
        .map(arrayItem -> {
          JsonObject jsonItem = new JsonObject(arrayItem.getString(0));
          ProfileSnapshotItem snapshotItem = new ProfileSnapshotItem();
          snapshotItem.setAssociationId(jsonItem.getString("association_id"));
          snapshotItem.setMasterId(jsonItem.getString("master_id"));
          snapshotItem.setDetailId(jsonItem.getString("detail_id"));
          snapshotItem.setDetailType(ContentType.fromValue(jsonItem.getString("detail_type")));
          snapshotItem.setDetail(jsonItem.getJsonArray("detail").getList().get(0));
          snapshotItem.setOrder(jsonItem.getInteger("detail_order"));
          if(!StringUtils.isEmpty(jsonItem.getString("react_to"))){
            snapshotItem.setReactTo(ProfileSnapshotWrapper.ReactTo.fromValue(jsonItem.getString("react_to")));
          }
          return snapshotItem;
        })
        .collect(Collectors.toList()));
  }
}
