package org.folio.services;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MappingProfileServiceImpl extends AbstractProfileService<MappingProfile, MappingProfileCollection> {

  @Override
  MappingProfile setProfileId(MappingProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<MappingProfile> setUserInfoForProfile(MappingProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
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
  protected ProfileSnapshotWrapper.ContentType getProfileContentType() {
    return ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
  }

  @Override
  protected Future<MappingProfileCollection> fetchRelations(MappingProfileCollection profileCollection, String query, int offset, int limit, String tenantId) {
    Future<MappingProfileCollection> result = Future.future();
    List<Future> futureList = new ArrayList<>();
    profileCollection.getMappingProfiles().forEach(profile ->
      futureList.add(fetchChildProfiles(profile, query, offset, limit, tenantId)
        .compose(childProfiles -> {
          profile.setChildProfiles(childProfiles);
          return Future.succeededFuture();
        })
        .compose(v -> fetchParentProfiles(profile, query, offset, limit, tenantId))
        .compose(parentProfiles -> {
          profile.setParentProfiles(parentProfiles);
          return Future.succeededFuture();
        }))
    );
    CompositeFuture.all(futureList).setHandler(ar -> {
      if (ar.succeeded()) {
        result.complete(profileCollection);
      } else {
        logger.error("Error during fetching related profiles", ar.cause());
        result.fail(ar.cause());
      }
    });
    return result;
  }

}
