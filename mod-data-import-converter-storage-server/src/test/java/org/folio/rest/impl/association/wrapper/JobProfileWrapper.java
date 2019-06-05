package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

/**
 * Wrapper for {@link JobProfile} entity
 */
public class JobProfileWrapper implements ProfileWrapper<JobProfile> {

  private JobProfile jobProfile;

  public JobProfileWrapper(JobProfile jobProfile) {
    this.jobProfile = jobProfile;
  }

  @Override
  public String getId() {
    return jobProfile.getId();
  }

  @Override
  public String getName() {
    return jobProfile.getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return jobProfile.getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return jobProfile.getMetadata();
  }

  @Override
  public JobProfile getProfile() {
    return jobProfile;
  }

  @Override
  public void setProfile(JobProfile profile) {
    this.jobProfile = profile;
  }

  @Override
  public Class<JobProfile> getProfileType() {
    return JobProfile.class;
  }
}
