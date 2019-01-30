package org.folio.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.folio.dao.JobProfileDaoImpl;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;

import java.util.Optional;
import java.util.UUID;

public class JobProfileServiceImpl implements ProfileService <JobProfile, JobProfileCollection> {

  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;

  public JobProfileServiceImpl(Vertx vertx, String tenantId) {
    this.jobProfileDao = new JobProfileDaoImpl(vertx, tenantId);
  }

  @Override
  public Future<JobProfileCollection> getProfiles(String query, int offset, int limit) {
    return jobProfileDao.getProfiles(query, offset, limit);
  }

  @Override
  public Future<Optional<JobProfile>> getProfileById(String id) {
    return jobProfileDao.getProfileById(id);
  }

  @Override
  public Future<JobProfile> saveProfile(JobProfile profile) {
    profile.setId(UUID.randomUUID().toString());
    return jobProfileDao.saveProfile(profile).map(profile);
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile) {
    return jobProfileDao.updateProfile(profile);
  }

  @Override
  public Future<Boolean> deleteProfile(String id) {
    return jobProfileDao.deleteProfile(id);
  }
}
