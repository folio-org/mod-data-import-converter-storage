package org.folio.dao.snapshot;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.folio.dataimport.util.DaoUtil.constructCriteria;

/**
 * Implementation for Profile snapshot DAO
 */
@Repository
public class ProfileSnapshotDaoImpl implements ProfileSnapshotDao {
  protected static final String ID_FIELD = "'id'";
  private static final Logger logger = LoggerFactory.getLogger(ProfileSnapshotDaoImpl.class);
  private static final String TABLE_NAME = "profile_snapshots";
  private static final String GET_JOB_PROFILE_SNAPSHOT = "select get_job_profile_snapshot('%s');";
  protected PostgresClientFactory pgClientFactory;

  public ProfileSnapshotDaoImpl(@Autowired PostgresClientFactory pgClientFactory) {
    this.pgClientFactory = pgClientFactory;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId) {
    Future<Results<ProfileSnapshotWrapper>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(TABLE_NAME, ProfileSnapshotWrapper.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error querying {} by id", ProfileSnapshotWrapper.class.getSimpleName(), e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(wrappers -> wrappers.isEmpty() ? Optional.empty() : Optional.of(wrappers.get(0)));
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
          return snapshotItem;
        })
        .collect(Collectors.toList()));
  }
}
