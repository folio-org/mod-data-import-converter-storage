package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileUpdateDto;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

/**
 * Wrapper for {@link JobProfile} entity
 */
public class JobProfileWrapper implements ProfileWrapper<JobProfileUpdateDto> {

  private JobProfileUpdateDto jobProfile;

  public JobProfileWrapper(JobProfileUpdateDto jobProfile) {
    this.jobProfile = jobProfile;
  }

  @Override
  public String getId() {
    return jobProfile.getProfile().getId();
  }

  @Override
  public String getName() {
    return jobProfile.getProfile().getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return jobProfile.getProfile().getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return jobProfile.getProfile().getMetadata();
  }

  @Override
  public JobProfileUpdateDto getProfile() {
    return jobProfile;
  }

  @Override
  public void setProfile(JobProfileUpdateDto profile) {
    this.jobProfile = profile;
  }

  @Override
  public Class<JobProfileUpdateDto> getProfileType() {
    return JobProfileUpdateDto.class;
  }
}
