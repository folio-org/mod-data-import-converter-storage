package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.UpdateResult;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
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
 * Data access object for {@link JobProfile}
 */
@Component
public class JobProfileDaoImpl implements ProfileDao <JobProfile, JobProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(JobProfileDaoImpl.class);
  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String ID_FIELD = "'id'";

  @Autowired
  private PostgresClientFactory pgClientFactory;

  @Override
  public Future<JobProfileCollection> getProfiles(String query, int offset, int limit, String tenantId) {
    Future<Results<JobProfile>> future = Future.future();
    try {
      String[] fieldList = {"*"};
      CQLWrapper cql = getCQLWrapper(JOB_PROFILES_TABLE_NAME, query, limit, offset);
      pgClientFactory.createInstance(tenantId).get(JOB_PROFILES_TABLE_NAME, JobProfile.class, fieldList, cql, true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error while searching for Job Profiles", e);
      future.fail(e);
    }
    return future.map(results -> new JobProfileCollection()
      .withJobProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  public Future<Optional<JobProfile>> getProfileById(String id, String tenantId) {
    Future<Results<JobProfile>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(JOB_PROFILES_TABLE_NAME, JobProfile.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      logger.error("Error querying Job Profiles by id", e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(jobProfiles -> jobProfiles.isEmpty() ? Optional.empty() : Optional.of(jobProfiles.get(0)));
  }

  @Override
  public Future<String> saveProfile(JobProfile profile, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(JOB_PROFILES_TABLE_NAME, profile.getId(), profile, future.completer());
    return future;
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile, String tenantId) {
    Future<JobProfile> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, profile.getId());
      pgClientFactory.createInstance(tenantId).update(JOB_PROFILES_TABLE_NAME, profile, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          logger.error("Could not update Job Profile with id {}", profile.getId(), updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = String.format("Job Profile with id '%s' was not found", profile.getId());
          logger.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(profile);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating Job Profile with id {}", profile.getId(), e);
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    Future<UpdateResult> future = Future.future();
    pgClientFactory.createInstance(tenantId).delete(JOB_PROFILES_TABLE_NAME, id, future.completer());
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }
}
