package org.folio.dao.association;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.UpdateResult;
import org.folio.dao.PostgresClientFactory;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static org.folio.dao.util.DaoUtil.constructCriteria;

/**
 * Generic implementation of the of the {@link ProfileAssociationDao}
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
public abstract class AbstractProfileAssociationDao<M, D> implements ProfileAssociationDao<M, D> { //NOSOANR
  protected static final String ID_FIELD = "'id'"; //NOSONAR
  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileAssociationDao.class);
  @Autowired
  protected PostgresClientFactory pgClientFactory; //NOSONAR
  protected ProfileDao masterProfileDao;  //NOSONAR
  protected ProfileDao detailProfileDao;  //NOSONAR

  public AbstractProfileAssociationDao(ProfileDao masterProfileDao, ProfileDao detailProfileDao) {
    this.masterProfileDao = masterProfileDao;
    this.detailProfileDao = detailProfileDao;
  }

  @Override
  public Future<String> save(ProfileAssociation entity, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(getTableName(), entity.getId(), entity, future.completer());
    return future;
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, String tenantId) {
    Future<Results<ProfileAssociation>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(getTableName(), ProfileAssociation.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error querying {} by id", ProfileAssociation.class.getSimpleName(), e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(profiles -> profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0)));
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, String tenantId) {
    Future<ProfileAssociation> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, entity.getId());
      pgClientFactory.createInstance(tenantId).update(getTableName(), entity, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          logger.error("Could not update {} with id {}", ProfileAssociation.class, entity.getId(), updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = String.format("%s with id '%s' was not found", ProfileAssociation.class, entity.getId());
          logger.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(entity);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating {} with id {}", ProfileAssociation.class, entity.getId(), e);
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<Boolean> delete(String id, String tenantId) {
    Future<UpdateResult> future = Future.future();
    pgClientFactory.createInstance(tenantId).delete(getTableName(), id, future.completer());
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }

  @Override
  public Future<D> getDetailProfilesByMasterId(String masterId) {
    // STUB implementation, use detailProfileDao to obtain "detail" profiles
    return Future.succeededFuture();
  }

  @Override
  public Future<M> getMasterProfilesByDetailId(String detailId) {
    // STUB implementation, use masterProfileDao to obtain "master" profiles
    return Future.succeededFuture();
  }

  /**
   * Provides access to the table name
   *
   * @return table name
   */
  abstract String getTableName();
}
