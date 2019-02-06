package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
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

  @Override
  Future<MappingProfile> setUserInfoForProfile(MappingProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
  }
}
