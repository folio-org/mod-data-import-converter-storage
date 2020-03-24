package org.folio.dao.snapshot;

import io.vertx.core.Future;
import io.vertx.core.Promise;
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
public class ProfileSnapshotDaoImpl implements ProfileSnapshotDao {
  private static final Logger logger = LoggerFactory.getLogger(ProfileSnapshotDaoImpl.class);
  private static final String TABLE_NAME = "profile_snapshots";
  private static final String GET_PROFILE_SNAPSHOT = "select get_profile_snapshot('%s', '%s', '%s');";
  protected PostgresClientFactory pgClientFactory;

  public ProfileSnapshotDaoImpl(@Autowired PostgresClientFactory pgClientFactory) {
    this.pgClientFactory = pgClientFactory;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId) {
    Promise<ProfileSnapshotWrapper> promise = Promise.promise();
    try {
      pgClientFactory.createInstance(tenantId).getById(TABLE_NAME, id, ProfileSnapshotWrapper.class, promise);
    } catch (Exception e) {
      logger.error("Error querying {} by id", ProfileSnapshotWrapper.class.getSimpleName(), e);
      promise.fail(e);
    }
    return promise.future()
      .map(wrapper -> wrapper == null ? Optional.empty() : Optional.of(wrapper));
  }

  @Override
  public Future<String> save(ProfileSnapshotWrapper entity, String tenantId) {
    Promise<String> promise = Promise.promise();
    pgClientFactory.createInstance(tenantId).save(TABLE_NAME, entity.getId(), entity, promise);
    return promise.future();
  }

  public Future<List<ProfileSnapshotItem>> getSnapshotItems(String profileId, ContentType profileType, String tenantId) {
    Promise<ResultSet> promise = Promise.promise();
    try {
      SnapshotProfileType snapshotProfileType = SnapshotProfileType.valueOf(profileType.value());
      String createSnapshotQuery = String.format(GET_PROFILE_SNAPSHOT, profileId, profileType.value(), snapshotProfileType.getTableName());
      pgClientFactory.createInstance(tenantId).select(createSnapshotQuery, promise);
    } catch (Exception e) {
      promise.fail(e);
    }
    return promise.future()
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
