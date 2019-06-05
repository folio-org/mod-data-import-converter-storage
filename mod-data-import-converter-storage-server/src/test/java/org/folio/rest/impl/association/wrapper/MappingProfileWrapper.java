package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;


/**
 * Wrapper for {@link MappingProfile} entity
 */
public class MappingProfileWrapper implements ProfileWrapper<MappingProfile> {

  private MappingProfile mappingProfile;

  public MappingProfileWrapper(MappingProfile mappingProfile) {
    this.mappingProfile = mappingProfile;
  }

  @Override
  public String getId() {
    return mappingProfile.getId();
  }

  @Override
  public String getName() {
    return mappingProfile.getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return mappingProfile.getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return mappingProfile.getMetadata();
  }

  @Override
  public MappingProfile getProfile() {
    return mappingProfile;
  }

  @Override
  public void setProfile(MappingProfile profile) {
    this.mappingProfile = profile;
  }

  @Override
  public Class<MappingProfile> getProfileType() {
    return MappingProfile.class;
  }
}
