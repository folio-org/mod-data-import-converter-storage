package org.folio.services;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.DataType;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JobProfileServiceImpl extends AbstractProfileService<JobProfile, JobProfileCollection> {

  @Override
  public Future<JobProfile> saveProfile(JobProfile profile, String tenantId) {
    profile.setDataTypes(sortDataTypes(profile.getDataTypes()));
    return profileDao.saveProfile(setProfileId(profile), tenantId).map(profile);
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile, String tenantId) {
    profile.setDataTypes(sortDataTypes(profile.getDataTypes()));
    return profileDao.updateProfile(profile, tenantId);
  }

  @Override
  JobProfile setProfileId(JobProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  private List<DataType> sortDataTypes(List<DataType> list) {
    if (list == null) {
      return Collections.emptyList();
    }
    Collections.sort(list);
    return list;
  }
}
