package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
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

  @Override
  Future<MatchProfile> setUserInfoForProfile(MatchProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
  }

  @Override
  MatchProfile markProfileAsDeleted(MatchProfile profile) {
    return profile.withDeleted(true);
  }

  @Override
  protected String getProfileName(MatchProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(MatchProfile profile) {
    return profile.getId();
  }

}
