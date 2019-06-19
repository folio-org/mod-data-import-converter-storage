package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link JobProfile}
 */
@Component
public class JobProfileDaoImpl extends AbstractProfileDao <JobProfile, JobProfileCollection> {

  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";
  private static final String JOB_TO_ACTION_ASSOCIATION_TABLE = "job_to_action_profiles";
  private static final String JOB_TO_MATCH_ASSOCIATION_TABLE = "job_to_match_profiles";

  @Override
  String getTableName() {
    return JOB_PROFILES_TABLE_NAME;
  }

  @Override
  Future<JobProfileCollection> mapResultsToCollection(Future<Results<JobProfile>> resultsFuture) {
    return resultsFuture.map(results -> new JobProfileCollection()
      .withJobProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  Class<JobProfile> getProfileType() {
    return JobProfile.class;
  }

  @Override
  String getProfileId(JobProfile profile) {
    return profile.getId();
  }

  @Override
  protected JobProfile markProfileEntityAsDeleted(JobProfile profile) {
    return profile.withDeleted(true);
  }

  @Override
  protected Future<Boolean> deleteAllAssociationsWithDetails(Future<SQLConnection> txConnection, String masterProfileId, String tenantId) {
    return deleteAssociationsWithDetails(txConnection, masterProfileId, JOB_TO_ACTION_ASSOCIATION_TABLE, tenantId)
      .compose(voidDeleteRes -> deleteAssociationsWithDetails(txConnection, masterProfileId, JOB_TO_MATCH_ASSOCIATION_TABLE, tenantId));
  }
}
