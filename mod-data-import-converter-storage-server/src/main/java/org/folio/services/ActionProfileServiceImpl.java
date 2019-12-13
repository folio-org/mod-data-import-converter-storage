package org.folio.services;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ActionProfileServiceImpl extends AbstractProfileService<ActionProfile, ActionProfileCollection> {

  @Override
  ActionProfile setProfileId(ActionProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<ActionProfile> setUserInfoForProfile(ActionProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
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
  protected Future<ActionProfileCollection> fetchRelations(ActionProfileCollection profileCollection, String query, int offset, int limit, String tenantId) {
    Future<ActionProfileCollection> result = Future.future();
    List<Future> futureList = new ArrayList<>();
    profileCollection.getActionProfiles().forEach(profile -> {
      String profileId = profile.getId();
      futureList.add(
        associationService.findDetails(profileId, ProfileSnapshotWrapper.ContentType.ACTION_PROFILE, null, query, offset, limit, tenantId)
          .compose(childProfiles -> {
            childProfiles.ifPresent(profileSnapshotWrapper -> profile.setChildProfiles(profileSnapshotWrapper.getChildSnapshotWrappers()
              .stream()
              .map(JsonObject::mapFrom)
              .map(json -> json.mapTo(ProfileSnapshotWrapper.class))
              .collect(Collectors.toList())));
            return Future.succeededFuture();
          }).compose(v -> associationService.findMasters(profileId, ProfileSnapshotWrapper.ContentType.ACTION_PROFILE, null, query, offset, limit, tenantId))
          .compose(parentProfiles -> {
            parentProfiles.ifPresent(profileSnapshotWrapper -> profile.setChildProfiles(profileSnapshotWrapper.getChildSnapshotWrappers()
              .stream()
              .map(JsonObject::mapFrom)
              .map(json -> json.mapTo(ProfileSnapshotWrapper.class))
              .collect(Collectors.toList())));
            return Future.succeededFuture();
          }));
    });
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
