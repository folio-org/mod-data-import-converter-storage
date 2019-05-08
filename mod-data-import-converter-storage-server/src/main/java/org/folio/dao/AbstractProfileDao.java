package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

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
      CQLWrapper cql = getCQLWrapper(getTableName(), query, limit, offset);
      if (!showDeleted) {
        cql.addWrapper(cql.addWrapper(getCQLWrapper(getTableName(), "deleted=false")));
      }
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
