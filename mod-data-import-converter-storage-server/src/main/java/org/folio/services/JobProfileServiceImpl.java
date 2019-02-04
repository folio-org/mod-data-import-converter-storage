package org.folio.services;

import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobProfileServiceImpl extends AbstractProfileService<JobProfile, JobProfileCollection> {

  @Override
  JobProfile setProfileId(JobProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }
}
