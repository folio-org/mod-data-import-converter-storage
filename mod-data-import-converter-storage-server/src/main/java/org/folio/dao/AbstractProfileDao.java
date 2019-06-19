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

  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileDao.class);
  private static final String ID_FIELD = "'id'";
  public static final String MASTER_PROFILE_ID_FIELD = "'masterProfileId'";

  @Autowired
  protected PostgresClientFactory pgClientFactory;
  public static final String IS_PROFILE_ASSOCIATED_AS_DETAIL_BY_ID_SQL = "SELECT exists (SELECT association_id FROM associations_view WHERE detail_id = '%s')";

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
          String errorMessage = format("%s with id '%s' was not found", className, profileId);
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
        ? Future.failedFuture(new NotFoundException(format("%s with id '%s' was not found", getProfileType().getSimpleName(), profileId)))
        : Future.succeededFuture(profileList.getResults().get(0)))
      .compose(profileMutator::apply)
      .compose(mutatedProfile -> updateProfile(tx, profileId, mutatedProfile, tenantId))
      .setHandler(updateAr -> {
        if (updateAr.succeeded()) {
          pgClientFactory.createInstance(tenantId).endTx(tx, endTx -> resultFuture.complete(updateAr.result()));
        } else {
          pgClientFactory.createInstance(tenantId).rollbackTx(tx, rollbackAr -> {
            String message = format("Rollback transaction. Error during %s update by id: %s ", getProfileType().getSimpleName(), profileId);
            logger.error(message, updateAr.cause());
            resultFuture.fail(updateAr.cause());
          });
        }
      });
      return resultFuture;
  }

  protected Future<T> updateProfile(AsyncResult<SQLConnection> tx, String profileId, T profile, String tenantId) {
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
      .append("' AND jsonb ->> 'deleted' ='false' ")
      .append(" LIMIT 1;");
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

  @Override
  public Future<Boolean> isProfileAssociatedAsDetail(String profileId, String tenantId) {
    Future<Boolean> future = Future.future();
    String preparedSql = format(IS_PROFILE_ASSOCIATED_AS_DETAIL_BY_ID_SQL, profileId);
    pgClientFactory.createInstance((tenantId)).select(preparedSql, selectAr -> {
      if (selectAr.succeeded()) {
        future.complete(selectAr.result().getResults().get(0).getBoolean(0));
      } else {
        logger.error("Error during retrieving associations for particular profile by its id. Profile id {}", profileId, selectAr.cause());
        future.fail(selectAr.cause());
      }
    });
    return future;
  }

  @Override
  public Future<Boolean> markProfileAsDeleted(String profileId, String tenantId) {
    Future<Boolean> future = Future.future();
    Future<SQLConnection> tx = Future.future();
    Criteria idCrit = constructCriteria(ID_FIELD, profileId);
    PostgresClient pgClient = pgClientFactory.createInstance(tenantId);

    pgClient.startTx(tx);
    tx.compose(sqlConnection -> {
      Future<Results<T>> selectFuture = Future.future();
      pgClient.get(tx, getTableName(), getProfileType(), new Criterion(idCrit), true, false, selectFuture);
      return selectFuture;
    }).compose(profileList -> profileList.getResults().isEmpty()
        ? Future.failedFuture(new NotFoundException(format("%s with id '%s' was not found", getProfileType().getSimpleName(), profileId)))
        : Future.succeededFuture(profileList.getResults().get(0)))
      .map(this::markProfileEntityAsDeleted)
      .compose(mutatedProfile -> updateProfile(tx, profileId, mutatedProfile, tenantId))
      .compose(updatedProfile -> deleteAllAssociationsWithDetails(tx, profileId, tenantId))
      .setHandler(ar -> {
        if (ar.succeeded()) {
          pgClient.endTx(tx, endTx -> future.complete(true));
        } else {
          pgClient.rollbackTx(tx, rollbackAr -> {
            String message = format("Rollback transaction. Error during mark %s as deleted by id: %s ", getProfileType().getSimpleName(), profileId);
            logger.error(message, ar.cause());
            future.fail(ar.cause());
          });
        }
      });
    return future;
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
   * @param profileId profile id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  protected abstract Future<Boolean> deleteAllAssociationsWithDetails(Future<SQLConnection> txConnection, String profileId, String tenantId);

  /**
   * Deletes associations of certain profile with other detail-profiles by its id in specified table.
   *
   * @param txConnection future with connection which will be used to perform deletion
   * @param profileId profile id
   * @param associationsTableName table name in which delete associations
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  protected Future<Boolean> deleteAssociationsWithDetails(Future<SQLConnection> txConnection, String profileId, String associationsTableName, String tenantId) {
    Future<Boolean> future = Future.future();
    PostgresClient pgClient = pgClientFactory.createInstance(tenantId);
    Criteria masterIdCrit = constructCriteria(MASTER_PROFILE_ID_FIELD, profileId);
    pgClient.delete(txConnection, associationsTableName, new Criterion(masterIdCrit), deleteAr -> {
      if (deleteAr.failed()) {
        logger.error("Error during delete associations of profile with other detail-profiles by its id '{}'", deleteAr.cause(), profileId);
        future.fail(deleteAr.cause());
      } else {
        future.complete(true);
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
