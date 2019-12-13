package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.List;
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
  protected String getProfileName(MatchProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(MatchProfile profile) {
    return profile.getId();
  }

  @Override
  protected ProfileSnapshotWrapper.ContentType getProfileContentType() {
    return ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;
  }

  @Override
  protected void setChildProfiles(MatchProfile profile, List<ProfileSnapshotWrapper> childProfiles) {
    profile.setChildProfiles(childProfiles);
  }

  @Override
  protected void setParentProfiles(MatchProfile profile, List<ProfileSnapshotWrapper> parentProfiles) {
    profile.setParentProfiles(parentProfiles);
  }

  @Override
  protected List<MatchProfile> getProfilesList(MatchProfileCollection profilesCollection) {
    return profilesCollection.getMatchProfiles();
  }

}
