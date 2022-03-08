package org.folio.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.cql2pgjson.CQL2PgJSON;
import org.folio.cql2pgjson.exception.FieldException;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.SQLConnection;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static org.folio.dao.util.DaoUtil.constructCriteria;
import static org.folio.dao.util.DaoUtil.getCQLWrapper;

/**
 * Generic implementation of the {@link ProfileDao}
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public abstract class AbstractProfileDao<T, S> implements ProfileDao<T, S> {

  private static final Logger logger = LogManager.getLogger();
  private static final String ID_FIELD = "'id'";

  @Autowired
  protected PostgresClientFactory pgClientFactory;
  public static final String IS_PROFILE_ASSOCIATED_AS_DETAIL_BY_ID_SQL = "SELECT exists (SELECT association_id FROM associations_view WHERE detail_id = '%s')";

  @Override
  public Future<S> getProfiles(boolean showDeleted, boolean showHidden, String query, int offset, int limit, String tenantId) {
    Promise<Results<T>> promise = Promise.promise();
    try {
      String[] fieldList = {"*"};
      CQLWrapper cql = getCQLWrapper(getTableName(), query, limit, offset);
      if (!showDeleted) {
        var notDeletedProfilesFilter = "deleted==false";
        cql.addWrapper(getCQLWrapper(getTableName(), notDeletedProfilesFilter));
      }
      if (!showHidden) {
        var notHiddenProfilesFilter = "hidden==false";
        cql.addWrapper(getCQLWrapper(getTableName(), notHiddenProfilesFilter));
      }
      pgClientFactory.createInstance(tenantId).get(getTableName(), getProfileType(), fieldList, cql, true, false, promise);
    } catch (Exception e) {
      logger.error("Error while searching for {}", getProfileType().getSimpleName(), e);
      promise.fail(e);
    }
    return mapResultsToCollection(promise.future());
  }

  @Override
  public Future<Optional<T>> getProfileById(String id, String tenantId) {
    Promise<Results<T>> promise = Promise.promise();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(getTableName(), getProfileType(), new Criterion(idCrit), true, false, promise);
    } catch (Exception e) {
      logger.error("Error querying {} by id", getProfileType().getSimpleName(), e);
      promise.fail(e);
    }
    return promise.future()
      .map(Results::getResults)
      .map(profiles -> profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0)));
  }

  @Override
  public Future<String> saveProfile(T profile, String tenantId) {
    Promise<String> promise = Promise.promise();
    pgClientFactory.createInstance(tenantId).save(getTableName(), getProfileId(profile), profile, promise);
    return promise.future();
  }

  @Override
  public Future<T> updateProfile(T profile, String tenantId) {
    Promise<T> promise = Promise.promise();
    String profileId = getProfileId(profile);
    String className = getProfileType().getSimpleName();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, getProfileId(profile));
      pgClientFactory.createInstance(tenantId).update(getTableName(), profile, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          logger.error("Could not update {} with id {}", className, profileId, updateResult.cause());
          promise.fail(updateResult.cause());
        } else if (updateResult.result().rowCount() != 1) {
          String errorMessage = format("%s with id '%s' was not found", className, profileId);
          logger.error(errorMessage);
          promise.fail(new NotFoundException(errorMessage));
        } else {
          promise.complete(profile);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating {} with id {}", className, profileId, e);
      promise.fail(e);
    }
    return promise.future();
  }

  @Override
  public Future<T> updateBlocking(String profileId, Function<T, Future<T>> profileMutator, String tenantId) {
    Promise<T> promise = Promise.promise();
    Promise<SQLConnection> tx = Promise.promise();
    Criteria idCrit = constructCriteria(ID_FIELD, profileId);
    pgClientFactory.createInstance(tenantId).startTx(tx);
    tx.future()
      .compose(sqlConnection -> {
        Promise<Results<T>> selectPromise = Promise.promise();
        pgClientFactory.createInstance(tenantId).get(tx.future(), getTableName(), getProfileType(), new Criterion(idCrit), true, false, selectPromise);
        return selectPromise.future();
      })
      .compose(profileList -> profileList.getResults().isEmpty()
        ? Future.failedFuture(new NotFoundException(format("%s with id '%s' was not found", getProfileType().getSimpleName(), profileId)))
        : Future.succeededFuture(profileList.getResults().get(0)))
      .compose(profileMutator)
      .compose(mutatedProfile -> updateProfile(tx.future(), profileId, mutatedProfile, tenantId))
      .onComplete(updateAr -> {
        if (updateAr.succeeded()) {
          pgClientFactory.createInstance(tenantId).endTx(tx.future(), endTx -> promise.complete(updateAr.result()));
        } else {
          pgClientFactory.createInstance(tenantId).rollbackTx(tx.future(), rollbackAr -> {
            String message = format("Rollback transaction. Error during %s update by id: %s ", getProfileType().getSimpleName(), profileId);
            logger.error(message, updateAr.cause());
            promise.fail(updateAr.cause());
          });
        }
      });
    return promise.future();
  }

  protected Future<T> updateProfile(AsyncResult<SQLConnection> tx, String profileId, T profile, String tenantId) {
    Promise<RowSet<Row>> promise = Promise.promise();
    try {
      CQLWrapper updateFilter = new CQLWrapper(new CQL2PgJSON(getTableName()), "id==" + profileId);
      pgClientFactory.createInstance(tenantId).update(tx, getTableName(), profile, updateFilter, true, promise);
    } catch (FieldException e) {
      logger.error("Error during updating {} by ID ", getProfileType(), e);
      promise.fail(e);
    }
    return promise.future().map(profile);
  }

  @Override
  public Future<Boolean> isProfileExistByName(String profileName, String profileId, String tenantId) {
    Promise<Boolean> promise = Promise.promise();
    PostgresClient client = pgClientFactory.createInstance(tenantId);
    StringBuilder selectQuery = new StringBuilder("SELECT jsonb FROM ") //NOSONAR
      .append(PostgresClient.convertToPsqlStandard(tenantId))
      .append(".")
      .append(getTableName())
      .append(" WHERE trim(both ' ' from lower(jsonb ->> 'name')) ='")
      .append(profileName.toLowerCase().trim())
      .append("' AND jsonb ->>").append(ID_FIELD).append("!= '").append(profileId)
      .append("' AND jsonb ->> 'deleted' = 'false' ")
      .append(" LIMIT 1;");
    client.select(selectQuery.toString(), reply -> {
      if (reply.succeeded()) {
        promise.complete(reply.result().rowCount() > 0);
      } else {
        logger.error("Error during counting profiles by its name. Profile name {}", profileName, reply.cause());
        promise.fail(reply.cause());
      }
    });
    return promise.future();
  }

  @Override
  public Future<Boolean> isProfileAssociatedAsDetail(String profileId, String tenantId) {
    Promise<Boolean> promise = Promise.promise();
    String preparedSql = format(IS_PROFILE_ASSOCIATED_AS_DETAIL_BY_ID_SQL, profileId);
    pgClientFactory.createInstance((tenantId)).select(preparedSql, selectAr -> {
      if (selectAr.succeeded()) {
        promise.complete(selectAr.result().iterator().next().getBoolean(0));
      } else {
        logger.error("Error during retrieving associations for particular profile by its id. Profile id {}", profileId, selectAr.cause());
        promise.fail(selectAr.cause());
      }
    });
    return promise.future();
  }

  @Override
  public Future<Boolean> markProfileAsDeleted(String profileId, String tenantId) {
    Promise<Boolean> promise = Promise.promise();
    Promise<SQLConnection> tx = Promise.promise();
    Criteria idCrit = constructCriteria(ID_FIELD, profileId);
    PostgresClient pgClient = pgClientFactory.createInstance(tenantId);

    pgClient.startTx(tx);
    tx.future()
      .compose(sqlConnection -> {
        // updating profile field 'deleted' to true in DB
        Promise<Results<T>> selectPromise = Promise.promise();
        pgClient.get(tx.future(), getTableName(), getProfileType(), new Criterion(idCrit), true, false, selectPromise);
        return selectPromise.future();
      })
      .compose(profileList -> profileList.getResults().isEmpty()
        ? Future.failedFuture(new NotFoundException(format("%s with id '%s' was not found", getProfileType().getSimpleName(), profileId)))
        : Future.succeededFuture(profileList.getResults().get(0)))
      .map(this::markProfileEntityAsDeleted)
      .compose(markedProfile -> updateProfile(tx.future(), profileId, markedProfile, tenantId))

      // deletion all associations of marked profile with other detail-profiles
      .compose(updatedProfile -> deleteAssociationsWithDetails(tx.future(), profileId, tenantId))
      .onComplete(ar -> {
        if (ar.succeeded()) {
          pgClient.endTx(tx.future(), endTx -> promise.complete(true));
        } else {
          pgClient.rollbackTx(tx.future(), rollbackAr -> {
            String message = format("Rollback transaction. Error during mark %s as deleted by id: %s ", getProfileType().getSimpleName(), profileId);
            logger.error(message, ar.cause());
            promise.fail(ar.cause());
          });
        }
      });
    return promise.future();
  }

  /**
   * Sets deleted to {@code true} in Profile entity
   *
   * @param profile Profile entity
   * @return Profile entity marked as deleted
   */
  protected abstract T markProfileEntityAsDeleted(T profile);

  /**
   * Deletes all associations of certain profile with other detail-profiles by its id.
   *
   * @param txConnection future with connection which will be used to perform deletion
   * @param profileId    profile id
   * @param tenantId     tenant id
   * @return future with true if succeeded
   */
  protected Future<Boolean> deleteAssociationsWithDetails(Future<SQLConnection> txConnection, String profileId, String tenantId) {
    Promise<Boolean> promise = Promise.promise();
    PostgresClient pgClient = pgClientFactory.createInstance(tenantId);
    String deleteQuery = String.format("DELETE FROM associations_view WHERE master_id = '%s'", profileId);
    pgClient.execute(txConnection, deleteQuery, deleteAr -> {
      if (deleteAr.failed()) {
        logger.error("Error during delete associations of profile with other detail-profiles by its id '{}'", profileId, deleteAr.cause());
        promise.fail(deleteAr.cause());
      } else {
        promise.complete(true);
      }
    });
    return promise.future();
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
