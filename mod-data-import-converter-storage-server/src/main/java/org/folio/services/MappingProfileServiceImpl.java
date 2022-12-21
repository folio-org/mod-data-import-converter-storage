package org.folio.services;

import io.vertx.core.Future;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.okapi.common.GenericCompositeFuture;
import org.folio.rest.impl.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.folio.rest.jaxrs.model.ProfileAssociation.MasterProfileType.ACTION_PROFILE;

@Component
public class MappingProfileServiceImpl extends AbstractProfileService<MappingProfile, MappingProfileCollection, MappingProfileUpdateDto> {

  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public Future<MappingProfile> saveProfile(MappingProfileUpdateDto profileDto, OkapiConnectionParams params) {
    return deleteExistingActionToMappingAssociations(profileDto, params.getTenantId())
      .compose(deleteAr -> super.saveProfile(profileDto, params));
  }

  @Override
  public Future<MappingProfile> updateProfile(MappingProfileUpdateDto profileDto, OkapiConnectionParams params) {
    return deleteExistingActionToMappingAssociations(profileDto, params.getTenantId())
      .compose(deleteAr -> super.updateProfile(profileDto, params));
  }

  @Override
  MappingProfile setProfileId(MappingProfile profile) {
    String profileId = profile.getId();
    return profile.withId(StringUtils.isBlank(profileId) ?
      UUID.randomUUID().toString() : profileId);
  }

  @Override
  Future<MappingProfile> setUserInfoForProfile(MappingProfileUpdateDto profile, OkapiConnectionParams params) {
    return lookupUser(profile.getProfile().getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.getProfile().withUserInfo(userInfo)));
  }

  @Override
  protected String getProfileName(MappingProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(MappingProfile profile) {
    return profile.getId();
  }

  @Override
  protected MappingProfileUpdateDto prepareAssociations(MappingProfileUpdateDto profileDto) {
    profileDto.getAddedRelations().forEach(association -> {
      if (StringUtils.isEmpty(association.getMasterProfileId())) {
        association.setMasterProfileId(profileDto.getProfile().getId());
      }
      if (StringUtils.isEmpty(association.getDetailProfileId())) {
        association.setDetailProfileId(profileDto.getProfile().getId());
      }
    });
    return profileDto;
  }

  @Override
  protected ContentType getProfileContentType() {
    return ContentType.MAPPING_PROFILE;
  }

  @Override
  protected void setChildProfiles(MappingProfile profile, List<ProfileSnapshotWrapper> childProfiles) {
    profile.setChildProfiles(childProfiles);
  }

  @Override
  protected void setParentProfiles(MappingProfile profile, List<ProfileSnapshotWrapper> parentProfiles) {
    profile.setParentProfiles(parentProfiles);
  }

  @Override
  protected List<MappingProfile> getProfilesList(MappingProfileCollection profilesCollection) {
    return profilesCollection.getMappingProfiles();
  }

  @Override
  protected List<ProfileAssociation> getProfileAssociationToAdd(MappingProfileUpdateDto dto) {
    return dto.getAddedRelations();
  }

  @Override
  protected List<ProfileAssociation> getProfileAssociationToDelete(MappingProfileUpdateDto dto) {
    return dto.getDeletedRelations();
  }

  @Override
  protected MappingProfile getProfile(MappingProfileUpdateDto dto) {
    return dto.getProfile();
  }

  private Future<Boolean> deleteExistingActionToMappingAssociations(MappingProfileUpdateDto profileDto, String tenantId) {
    List<Future<Boolean>> futures = profileDto.getAddedRelations().stream()
      .filter(profileAssociation -> profileAssociation.getMasterProfileType().equals(ACTION_PROFILE))
      .map(ProfileAssociation::getMasterProfileId)
      .map(actionProfileId -> profileAssociationService.deleteByMasterId(actionProfileId, ContentType.ACTION_PROFILE,
        ContentType.MAPPING_PROFILE, tenantId))
      .collect(Collectors.toList());

    return GenericCompositeFuture.all(futures)
      .onFailure(th -> LOGGER.warn( "deleteExistingActionToMappingAssociations:: Failed to delete existing action-to-mapping associations", th))
      .map(true);
  }

}
