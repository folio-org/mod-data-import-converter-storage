package org.folio.services;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.ProfileDao;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.dataimport.util.RestUtil;
import org.folio.rest.jaxrs.model.EntityTypeCollection;
import org.folio.rest.jaxrs.model.UserInfo;
import org.folio.services.util.EntityTypes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Generic implementation of the {@link ProfileService}
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public abstract class AbstractProfileService<T, S> implements ProfileService<T, S> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileService.class);
  private static final String GET_USER_URL = "/users?query=id==";

  private final EntityTypeCollection entityTypeCollection;

  protected AbstractProfileService() {
    List<String> entityTypeList = Arrays.stream(EntityTypes.values())
      .map(EntityTypes::getName)
      .collect(Collectors.toList());
    entityTypeCollection = new EntityTypeCollection()
      .withEntityTypes(entityTypeList)
      .withTotalRecords(entityTypeList.size());
  }

  @Autowired
  private ProfileDao<T, S> profileDao;

  @Override
  public Future<S> getProfiles(boolean showDeleted, String query, int offset, int limit, String tenantId) {
    return profileDao.getProfiles(showDeleted, query, offset, limit, tenantId);
  }

  @Override
  public Future<Optional<T>> getProfileById(String id, String tenantId) {
    return profileDao.getProfileById(id, tenantId);
  }

  @Override
  public Future<T> saveProfile(T profile, OkapiConnectionParams params) {
    return setUserInfoForProfile(profile, params)
      .compose(profileWithInfo -> profileDao.saveProfile(setProfileId(profileWithInfo), params.getTenantId())
        .map(profileWithInfo));
  }

  @Override
  public Future<T> updateProfile(T profile, OkapiConnectionParams params) {
    return setUserInfoForProfile(profile, params)
      .compose(profileWithInfo -> profileDao.updateProfile(profileWithInfo, params.getTenantId()));
  }

  public Future<Boolean> markProfileAsDeleted(String id, String tenantId) {
    return profileDao.updateBlocking(id, profile -> Future.succeededFuture(markProfileAsDeleted(profile)), tenantId)
      .map(true);
  }

  @Override
  public Future<Boolean> isProfileExistByName(String profileName, String profileId, String tenantId) {
    return profileDao.isProfileExistByName(profileName, profileId, tenantId);
  }

  @Override
  public Future<EntityTypeCollection> getEntityTypes() {
    return Future.succeededFuture(entityTypeCollection);
  }

  /**
   * Generates id and sets it to the Profile entity
   *
   * @param profile Profile
   * @return Profile with generated id
   */
  abstract T setProfileId(T profile);

  /**
   * Set UserInfo for the Profile entity
   *
   * @param profile Profile
   * @param params  {@link OkapiConnectionParams}
   * @return Profile with filled userInfo field
   */
  abstract Future<T> setUserInfoForProfile(T profile, OkapiConnectionParams params);

  /**
   * Sets deleted to {@code true} in Profile entity
   *
   * @param profile Profile entity
   * @return Profile entity marked as deleted
   */
  abstract T markProfileAsDeleted(T profile);

  /**
   * Finds user by user id and returns UserInfo
   *
   * @param userId user id
   * @param params Okapi connection params
   * @return Future with found UserInfo
   */
  Future<UserInfo> lookupUser(String userId, OkapiConnectionParams params) {
    Future<UserInfo> future = Future.future();
    RestUtil.doRequest(params, GET_USER_URL + userId, HttpMethod.GET, null)
      .setHandler(getUserResult -> {
        if (RestUtil.validateAsyncResult(getUserResult, future)) {
          JsonObject response = getUserResult.result().getJson();
          if (!response.containsKey("totalRecords") || !response.containsKey("users")) {
            future.fail("Error, missing field(s) 'totalRecords' and/or 'users' in user response object");
          } else {
            int recordCount = response.getInteger("totalRecords");
            if (recordCount > 1) {
              String errorMessage = "There are more then one user by requested user id : " + userId;
              logger.error(errorMessage);
              future.fail(errorMessage);
            } else if (recordCount == 0) {
              String errorMessage = "No user found by user id :" + userId;
              logger.error(errorMessage);
              future.fail(errorMessage);
            } else {
              JsonObject jsonUser = response.getJsonArray("users").getJsonObject(0);
              JsonObject userPersonalInfo = jsonUser.getJsonObject("personal");
              UserInfo userInfo = new UserInfo()
                .withFirstName(userPersonalInfo.getString("firstName"))
                .withLastName(userPersonalInfo.getString("lastName"))
                .withUserName(jsonUser.getString("username"));
              future.complete(userInfo);
            }
          }
        }
      });
    return future;
  }

}
