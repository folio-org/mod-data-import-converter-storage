package org.folio.services;

import io.vertx.core.Future;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JobProfileServiceImpl implements ProfileService <JobProfile, JobProfileCollection> {

  @Autowired
  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;

  @Override
  public Future<JobProfileCollection> getProfiles(String query, int offset, int limit, String tenantId) {
    return jobProfileDao.getProfiles(query, offset, limit, tenantId);
  }

  @Override
  public Future<Optional<JobProfile>> getProfileById(String id, String tenantId) {
    return jobProfileDao.getProfileById(id, tenantId);
  }

  @Override
  public Future<JobProfile> saveProfile(JobProfile profile, String tenantId) {
    profile.setId(UUID.randomUUID().toString());
    return jobProfileDao.saveProfile(profile, tenantId).map(profile);
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile, String tenantId) {
    return jobProfileDao.updateProfile(profile, tenantId);
  }

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    return jobProfileDao.deleteProfile(id, tenantId);
  }
}
