package org.folio.dao;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link JobProfile}
 */
@Component
public class JobProfileDaoImpl extends AbstractProfileDao<JobProfile, JobProfileCollection> {

  private static final String JOB_PROFILES_TABLE_NAME = "job_profiles";

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
}
