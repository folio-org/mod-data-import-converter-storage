package org.folio.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.z3950.zing.cql.cql2pgjson.CQL2PgJSON;
import org.z3950.zing.cql.cql2pgjson.FieldException;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.function.Function;

import static org.folio.dao.util.DaoUtil.constructCriteria;
import static org.folio.dao.util.DaoUtil.getCQLWrapper;

/**
 * Generic implementation of the {@link ProfileDao}
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public abstract class AbstractProfileDao<T, S> implements ProfileDao<T, S> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileDao.class);
  private static final String ID_FIELD = "'id'";

  @Autowired
  private PostgresClientFactory pgClientFactory;

  @Override
  public Future<S> getProfiles(boolean showDeleted, String query, int offset, int limit, String tenantId) {
    Future<Results<T>> future = Future.future();
    try {
      String[] fieldList = {"*"};
      String notDeletedProfilesFilter = null;
      if (!showDeleted) {
        notDeletedProfilesFilter = "deleted=false";
      }
      CQLWrapper cql = getCQLWrapper(getTableName(), notDeletedProfilesFilter, limit, offset);
      cql.addWrapper(getCQLWrapper(getTableName(), query));
      pgClientFactory.createInstance(tenantId).get(getTableName(), getProfileType(), fieldList, cql, true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error while searching for {}", getProfileType().getSimpleName(), e);
      future.fail(e);
    }
    return mapResultsToCollection(future);
  }

  @Override
  public Future<Optional<T>> getProfileById(String id, String tenantId) {
    Future<Results<T>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(getTableName(), getProfileType(), new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error querying {} by id", getProfileType().getSimpleName(), e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(profiles -> profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0)));
  }

  @Override
  public Future<String> saveProfile(T profile, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(getTableName(), getProfileId(profile), profile, future.completer());
    return future;
  }

  @Override
  public Future<T> updateProfile(T profile, String tenantId) {
    Future<T> future = Future.future();
    String profileId = getProfileId(profile);
    String className = getProfileType().getSimpleName();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, getProfileId(profile));
      pgClientFactory.createInstance(tenantId).update(getTableName(), profile, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          logger.error("Could not update {} with id {}", className, profileId, updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = String.format("%s with id '%s' was not found", className, profileId);
          logger.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(profile);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating {} with id {}", className, profileId, e);
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<T> updateBlocking(String profileId, Function<T, Future<T>> profileMutator, String tenantId) {
    Future<T> resultFuture = Future.future();
    Future<SQLConnection> tx = Future.future();
    Criteria idCrit = constructCriteria(ID_FIELD, profileId);
    pgClientFactory.createInstance(tenantId).startTx(tx);
    tx.compose(sqlConnection -> {
      Future<Results<T>> selectFuture = Future.future();
      pgClientFactory.createInstance(tenantId).get(tx, getTableName(), getProfileType(), new Criterion(idCrit), true, false, selectFuture);
      return selectFuture;
    }).compose(profileList -> profileList.getResults().isEmpty()
        ? Future.failedFuture(new NotFoundException(String.format("%s with id '%s' was not found", getProfileType().getSimpleName(), profileId)))
        : Future.succeededFuture(profileList.getResults().get(0)))
      .compose(profileMutator::apply)
      .compose(mutatedProfile -> updateProfile(tx, profileId, mutatedProfile, tenantId))
      .setHandler(updateAr -> {
        if (updateAr.succeeded()) {
          pgClientFactory.createInstance(tenantId).endTx(tx, endTx -> resultFuture.complete(updateAr.result()));
        } else {
          pgClientFactory.createInstance(tenantId).rollbackTx(tx, rollbackAr -> {
            String message = String.format("Rollback transaction. Error during %s update by id: %s ", getProfileType().getSimpleName(), profileId);
            logger.error(message, updateAr.cause());
            resultFuture.fail(updateAr.cause());
          });
        }
      });
      return resultFuture;
  }

  private Future<T> updateProfile(AsyncResult<SQLConnection> tx, String profileId, T profile, String tenantId) {
    Future<UpdateResult> future = Future.future();
    try {
      CQLWrapper updateFilter = new CQLWrapper(new CQL2PgJSON(getTableName() + ".jsonb"), "id==" + profileId);
      pgClientFactory.createInstance(tenantId).update(tx, getTableName(), profile, updateFilter, true, future.completer());
    } catch (FieldException e) {
      logger.error("Error during updating {} by ID ", getProfileType(), e);
      future.fail(e);
    }
    return future.map(profile);
  }

  @Override
  public Future<Boolean> isProfileExistByName(String profileName, String profileId, String tenantId) {
    Future<Boolean> future = Future.future();
    PostgresClient client = pgClientFactory.createInstance(tenantId);
    StringBuilder selectQuery = new StringBuilder("SELECT jsonb FROM ") //NOSONAR
      .append(PostgresClient.convertToPsqlStandard(tenantId))
      .append(".")
      .append(getTableName())
      .append(" WHERE trim(both ' ' from lower(jsonb ->> 'name')) ='")
      .append(profileName.toLowerCase().trim())
      .append("' AND jsonb ->>").append(ID_FIELD).append("!= '").append(profileId)
      .append("' LIMIT 1;");
    client.select(selectQuery.toString(), reply -> {
      if (reply.succeeded()) {
        future.complete(reply.result().getNumRows() > 0);
      } else {
        logger.error("Error during counting profiles by its name. Profile name {}", profileName, reply.cause());
        future.fail(reply.cause());
      }
    });
    return future;
  }

  /**
   * Provides access to the table name
   *
   * @return table name
   */
  abstract String getTableName();

  /**
   * Maps results to S, a collection of T entities
   *
   * @param resultsFuture future parametrized by T Results
   * @return future with S, a collection of T entities
   */
  abstract Future<S> mapResultsToCollection(Future<Results<T>> resultsFuture);

  /**
   * Provides access to the Class of the Profile type
   *
   * @return Class
   */
  abstract Class<T> getProfileType();

  /**
   * Provides access to the profile id
   *
   * @param profile Profile
   * @return Profile id
   */
  abstract String getProfileId(T profile);
}
