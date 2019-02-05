package org.folio.services;

import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MappingProfileServiceImpl extends AbstractProfileService<MappingProfile, MappingProfileCollection> {

  @Override
  MappingProfile setProfileId(MappingProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }
}
