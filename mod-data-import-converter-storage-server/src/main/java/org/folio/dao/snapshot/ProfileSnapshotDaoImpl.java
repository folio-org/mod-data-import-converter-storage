package org.folio.dao.snapshot;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for Profile snapshot DAO
 */
@Repository
public class ProfileSnapshotDaoImpl implements ProfileSnapshotDao {
  private static final Logger logger = LogManager.getLogger();
  private static final String TABLE_NAME = "profile_snapshots";
  private static final String GET_PROFILE_SNAPSHOT = "select get_profile_snapshot('%s', '%s', '%s', '%s');";
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
      logger.warn("getById:: Error querying {} by id", ProfileSnapshotWrapper.class.getSimpleName(), e);
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

  public Future<List<ProfileSnapshotItem>> getSnapshotItems(String profileId, ContentType profileType, String jobProfileId, String tenantId) {
    Promise<RowSet<Row>> promise = Promise.promise();
    try {
      SnapshotProfileType snapshotProfileType = SnapshotProfileType.valueOf(profileType.value());
      String createSnapshotQuery = String.format(GET_PROFILE_SNAPSHOT, profileId, profileType.value(), snapshotProfileType.getTableName(), jobProfileId);
      pgClientFactory.createInstance(tenantId).select(createSnapshotQuery, promise);
    } catch (Exception e) {
      logger.warn("getSnapshotItems:: Error while getSnapshotItems", e);
      promise.fail(e);
    }
    return promise.future()
      .map(rows -> {
        List<ProfileSnapshotItem> snapshotItems = new ArrayList<>();
        rows.forEach(row -> {
          JsonObject jsonItem = row.get(JsonObject.class, 0);
          ProfileSnapshotItem snapshotItem = new ProfileSnapshotItem();
          snapshotItem.setAssociationId(jsonItem.getString("association_id"));
          snapshotItem.setMasterId(jsonItem.getString("master_id"));
          snapshotItem.setDetailId(jsonItem.getString("detail_id"));
          snapshotItem.setDetailType(ContentType.fromValue(jsonItem.getString("detail_type")));
          snapshotItem.setDetail(jsonItem.getJsonArray("detail").getList().get(0));
          snapshotItem.setOrder(jsonItem.getInteger("detail_order"));
          if (StringUtils.isNotEmpty(jsonItem.getString("react_to"))) {
            snapshotItem.setReactTo(ProfileSnapshotWrapper.ReactTo.fromValue(jsonItem.getString("react_to")));
          }
          snapshotItems.add(snapshotItem);
        });
        return snapshotItems;
      });
  }
}
