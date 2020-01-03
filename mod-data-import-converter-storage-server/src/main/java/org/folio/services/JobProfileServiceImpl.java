package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.JobProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JobProfileServiceImpl extends AbstractProfileService<JobProfile, JobProfileCollection, JobProfileUpdateDto> {

  @Override
  JobProfile setProfileId(JobProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<JobProfile> setUserInfoForProfile(JobProfileUpdateDto profile, OkapiConnectionParams params) {
    return lookupUser(profile.getProfile().getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.getProfile().withUserInfo(userInfo)));
  }

  @Override
  protected String getProfileName(JobProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(JobProfile profile) {
    return profile.getId();
  }

  @Override
  protected ProfileSnapshotWrapper.ContentType getProfileContentType() {
    return ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
  }

  @Override
  protected void setChildProfiles(JobProfile profile, List<ProfileSnapshotWrapper> childProfiles) {
    profile.setChildProfiles(childProfiles);
  }

  @Override
  protected void setParentProfiles(JobProfile profile, List<ProfileSnapshotWrapper> parentProfiles) {
    profile.setParentProfiles(parentProfiles);
  }

  @Override
  protected List<JobProfile> getProfilesList(JobProfileCollection profilesCollection) {
    return profilesCollection.getJobProfiles();
  }

}
