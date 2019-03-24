package org.folio.dao.snapshot;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.folio.dao.util.DaoUtil.constructCriteria;

@Repository
public class ProfileSnapshotDaoImpl implements ProfileSnapshotDao {
  protected static final String ID_FIELD = "'id'";
  private static final Logger logger = LoggerFactory.getLogger(ProfileSnapshotDaoImpl.class);
  private static final String TABLE_NAME = "profile_snapshots";

  @Autowired
  protected PostgresClientFactory pgClientFactory;

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
}
