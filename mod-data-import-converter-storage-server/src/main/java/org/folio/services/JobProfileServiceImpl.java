package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobProfileServiceImpl extends AbstractProfileService<JobProfile, JobProfileCollection> {

  @Override
  public Future<JobProfile> saveProfile(JobProfile profile, OkapiConnectionParams params) {
    return super.saveProfile(profile, params);
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile, OkapiConnectionParams params) {
    return super.updateProfile(profile, params);
  }

  @Override
  JobProfile setProfileId(JobProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<JobProfile> setUserInfoForProfile(JobProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
  }
}
