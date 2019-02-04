package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.UpdateResult;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static org.folio.dao.util.DaoUtil.constructCriteria;
import static org.folio.dao.util.DaoUtil.getCQLWrapper;

/**
 * Data access object for {@link MatchProfile}
 */
@Component
public class MatchProfileDaoImpl implements ProfileDao<MatchProfile, MatchProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(MatchProfileDaoImpl.class);
  private static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";
  private static final String ID_FIELD = "'id'";

  @Autowired
  private PostgresClientFactory pgClientFactory;

  @Override
  public Future<MatchProfileCollection> getProfiles(String query, int offset, int limit, String tenantId) {
    Future<Results<MatchProfile>> future = Future.future();
    try {
      String[] fieldList = {"*"};
      CQLWrapper cql = getCQLWrapper(MATCH_PROFILES_TABLE_NAME, query, limit, offset);
      pgClientFactory.createInstance(tenantId).get(MATCH_PROFILES_TABLE_NAME, MatchProfile.class, fieldList, cql, true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error while searching for Match Profiles", e);
      future.fail(e);
    }
    return future.map(results -> new MatchProfileCollection()
      .withMatchProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  public Future<Optional<MatchProfile>> getProfileById(String id, String tenantId) {
    Future<Results<MatchProfile>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(MATCH_PROFILES_TABLE_NAME, MatchProfile.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error querying Match Profiles by id", e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(matchProfiles -> matchProfiles.isEmpty() ? Optional.empty() : Optional.of(matchProfiles.get(0)));
  }

  @Override
  public Future<String> saveProfile(MatchProfile profile, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(MATCH_PROFILES_TABLE_NAME, profile.getId(), profile, future.completer());
    return future;
  }

  @Override
  public Future<MatchProfile> updateProfile(MatchProfile profile, String tenantId) {
    Future<MatchProfile> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, profile.getId());
      pgClientFactory.createInstance(tenantId).update(MATCH_PROFILES_TABLE_NAME, profile, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          logger.error("Could not update Match Profile with id {}", profile.getId(), updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = String.format("Match Profile with id '%s' was not found", profile.getId());
          logger.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(profile);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating Match Profile with id {}", profile.getId(), e);
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    Future<UpdateResult> future = Future.future();
    pgClientFactory.createInstance(tenantId).delete(MATCH_PROFILES_TABLE_NAME, id, future.completer());
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }
}
