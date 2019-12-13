package org.folio.services;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MatchProfileServiceImpl extends AbstractProfileService<MatchProfile, MatchProfileCollection> {

  @Override
  MatchProfile setProfileId(MatchProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<MatchProfile> setUserInfoForProfile(MatchProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
  }

  @Override
  protected String getProfileName(MatchProfile profile) {
    return profile.getName();
  }

  @Override
  protected String getProfileId(MatchProfile profile) {
    return profile.getId();
  }

  @Override
  protected Future<MatchProfileCollection> fetchRelations(MatchProfileCollection profileCollection, String query, int offset, int limit, String tenantId) {
    Future<MatchProfileCollection> result = Future.future();
    List<Future> futureList = new ArrayList<>();
    profileCollection.getMatchProfiles().forEach(profile -> {
      String profileId = profile.getId();
      futureList.add(
        associationService.findDetails(profileId, ProfileSnapshotWrapper.ContentType.MATCH_PROFILE, null, query, offset, limit, tenantId)
          .compose(childProfiles -> {
            childProfiles.ifPresent(profileSnapshotWrapper -> profile.setChildProfiles(profileSnapshotWrapper.getChildSnapshotWrappers()
              .stream()
              .map(JsonObject::mapFrom)
              .map(json -> json.mapTo(ProfileSnapshotWrapper.class))
              .collect(Collectors.toList())));
            return Future.succeededFuture();
          }).compose(v -> associationService.findMasters(profileId, ProfileSnapshotWrapper.ContentType.MATCH_PROFILE, null, query, offset, limit, tenantId))
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
