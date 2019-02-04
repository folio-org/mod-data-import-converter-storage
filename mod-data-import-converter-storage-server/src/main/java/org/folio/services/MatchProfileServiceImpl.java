package org.folio.services;

import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MatchProfileServiceImpl extends AbstractProfileService<MatchProfile, MatchProfileCollection> {

  @Override
  MatchProfile setProfileId(MatchProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }
}
