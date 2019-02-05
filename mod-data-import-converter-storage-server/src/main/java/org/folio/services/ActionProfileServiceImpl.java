package org.folio.services;

import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ActionProfileServiceImpl extends AbstractProfileService<ActionProfile, ActionProfileCollection> {

  @Override
  ActionProfile setProfileId(ActionProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }
}
