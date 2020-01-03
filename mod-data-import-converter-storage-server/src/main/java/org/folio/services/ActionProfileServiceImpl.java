package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ActionProfileServiceImpl extends AbstractProfileService<ActionProfile, ActionProfileCollection, ActionProfileUpdateDto> {

  @Override
  ActionProfile setProfileId(ActionProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<ActionProfile> setUserInfoForProfile(ActionProfileUpdateDto profile, OkapiConnectionParams params) {
    return lookupUser(profile.getProfile().getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.getProfile().withUserInfo(userInfo)));
  }

  @Override
  protected String getProfileName(ActionProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(ActionProfile profile) {
    return profile.getId();
  }

  @Override
  protected ProfileSnapshotWrapper.ContentType getProfileContentType() {
    return ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
  }

  @Override
  protected void setChildProfiles(ActionProfile profile, List<ProfileSnapshotWrapper> childProfiles) {
    profile.setChildProfiles(childProfiles);
  }

  @Override
  protected void setParentProfiles(ActionProfile profile, List<ProfileSnapshotWrapper> parentProfiles) {
    profile.setParentProfiles(parentProfiles);
  }

  @Override
  protected List<ActionProfile> getProfilesList(ActionProfileCollection profilesCollection) {
    return profilesCollection.getActionProfiles();
  }

}
