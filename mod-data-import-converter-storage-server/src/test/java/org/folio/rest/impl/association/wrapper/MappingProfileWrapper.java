package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;


/**
 * Wrapper for {@link MappingProfile} entity
 */
public class MappingProfileWrapper implements ProfileWrapper<MappingProfileUpdateDto> {

  private MappingProfileUpdateDto mappingProfile;

  public MappingProfileWrapper(MappingProfileUpdateDto mappingProfile) {
    this.mappingProfile = mappingProfile;
  }

  @Override
  public String getId() {
    return mappingProfile.getProfile().getId();
  }

  @Override
  public String getName() {
    return mappingProfile.getProfile().getName();
  }

  @Override
  public UserInfo getUserInfo() {
    return mappingProfile.getProfile().getUserInfo();
  }

  @Override
  public Metadata getMetadata() {
    return mappingProfile.getProfile().getMetadata();
  }

  @Override
  public MappingProfileUpdateDto getProfile() {
    return mappingProfile;
  }

  @Override
  public void setProfile(MappingProfileUpdateDto profile) {
    this.mappingProfile = profile;
  }

  @Override
  public Class<MappingProfileUpdateDto> getProfileType() {
    return MappingProfileUpdateDto.class;
  }
}
