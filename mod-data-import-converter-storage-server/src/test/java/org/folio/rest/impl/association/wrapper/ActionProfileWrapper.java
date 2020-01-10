package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

/**
 * Wrapper for {@link ActionProfileUpdateDto} entity
 */
public class ActionProfileWrapper implements ProfileWrapper<ActionProfileUpdateDto> {

  private ActionProfileUpdateDto actionProfile;

  public ActionProfileWrapper(ActionProfileUpdateDto actionProfile) {
    this.actionProfile = actionProfile;
  }

  @Override
  public String getId() {
    return actionProfile.getProfile().getId();
  }

  @Override
  public String getName() {
    return actionProfile.getProfile().getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return actionProfile.getProfile().getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return actionProfile.getProfile().getMetadata();
  }

  @Override
  public ActionProfileUpdateDto getProfile() {
    return actionProfile;
  }

  @Override
  public void setProfile(ActionProfileUpdateDto profile) {
    this.actionProfile = profile;
  }

  @Override
  public Class<ActionProfileUpdateDto> getProfileType() {
    return ActionProfileUpdateDto.class;
  }
}
