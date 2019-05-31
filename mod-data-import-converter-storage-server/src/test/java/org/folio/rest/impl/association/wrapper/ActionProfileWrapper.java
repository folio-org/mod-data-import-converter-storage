package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

public class ActionProfileWrapper implements ProfileWrapper<ActionProfile> {

  private ActionProfile actionProfile;

  public ActionProfileWrapper(ActionProfile jobProfile) {
    this.actionProfile = jobProfile;
  }

  @Override
  public String getId() {
    return actionProfile.getId();
  }

  @Override
  public String getName() {
    return actionProfile.getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return actionProfile.getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return actionProfile.getMetadata();
  }

  @Override
  public ActionProfile getProfile() {
    return actionProfile;
  }

  @Override
  public void setProfile(ActionProfile profile) {
    this.actionProfile = profile;
  }

  @Override
  public Class<ActionProfile> getProfileType() {
    return ActionProfile.class;
  }
}
