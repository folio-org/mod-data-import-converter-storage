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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generic implementation of the {@link ProfileService}
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public abstract class AbstractProfileService<T, S> implements ProfileService<T, S> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileService.class);
  private static final String GET_USER_URL = "/users?query=id==";

  protected static final String INVENTORY_HOLDINGS = "INVENTORY_HOLDINGS";
  protected static final String INVENTORY_INSTANCE = "INVENTORY_INSTANCE";
  protected static final String INVENTORY_ITEM = "INVENTORY_ITEM";
  protected static final String INVOICE = "INVOICE";
  protected static final String MARC_AUTHORITY_RECORD = "MARC_AUTHORITY_RECORD";
  protected static final String MARC_BIB_RECORD = "MARC_BIB_RECORD";
  protected static final String MARC_HOLDINGS_RECORD = "MARC_HOLDINGS_RECORD";
  protected static final String ORDER = "ORDER";
  protected static final String ERROR = "ERROR";

  private final EntityTypeCollection entityTypeCollection;

  protected AbstractProfileService() {
    List<String> entityTypeList = Arrays.asList(INVENTORY_HOLDINGS, INVENTORY_INSTANCE, INVENTORY_ITEM,
      INVOICE, MARC_AUTHORITY_RECORD, MARC_BIB_RECORD, MARC_HOLDINGS_RECORD, ORDER, ERROR);
    entityTypeCollection = new EntityTypeCollection()
      .withEntityTypes(entityTypeList)
      .withTotalRecords(entityTypeList.size());
  }

  @Autowired
  private ProfileDao<T, S> profileDao;

  @Override
  public Future<S> getProfiles(String query, int offset, int limit, String tenantId) {
    return profileDao.getProfiles(query, offset, limit, tenantId);
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

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    return profileDao.deleteProfile(id, tenantId);
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
