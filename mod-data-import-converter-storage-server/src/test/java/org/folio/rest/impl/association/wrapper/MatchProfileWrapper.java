package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileUpdateDto;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

/**
 * Wrapper for {@link MatchProfile} entity
 */
public class MatchProfileWrapper implements ProfileWrapper<MatchProfileUpdateDto> {

  private MatchProfileUpdateDto matchProfile;

  public MatchProfileWrapper(MatchProfileUpdateDto matchProfile) {
    this.matchProfile = matchProfile;
  }

  @Override
  public String getId() {
    return matchProfile.getProfile().getId();
  }

  @Override
  public String getName() {
    return matchProfile.getProfile().getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return matchProfile.getProfile().getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return matchProfile.getProfile().getMetadata();
  }

  @Override
  public MatchProfileUpdateDto getProfile() {
    return matchProfile;
  }

  @Override
  public void setProfile(MatchProfileUpdateDto profile) {
    this.matchProfile = profile;
  }

  @Override
  public Class<MatchProfileUpdateDto> getProfileType() {
    return MatchProfileUpdateDto.class;
  }
}
