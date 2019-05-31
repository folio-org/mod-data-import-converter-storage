package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

public class MatchProfileWrapper implements ProfileWrapper<MatchProfile> {

  private MatchProfile matchProfile;

  public MatchProfileWrapper(MatchProfile matchProfile) {
    this.matchProfile = matchProfile;
  }

  @Override
  public String getId() {
    return matchProfile.getId();
  }

  @Override
  public String getName() {
    return matchProfile.getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return matchProfile.getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return matchProfile.getMetadata();
  }

  @Override
  public MatchProfile getProfile() {
    return matchProfile;
  }

  @Override
  public void setProfile(MatchProfile profile) {
    this.matchProfile = profile;
  }

  @Override
  public Class<MatchProfile> getProfileType() {
    return MatchProfile.class;
  }
}
