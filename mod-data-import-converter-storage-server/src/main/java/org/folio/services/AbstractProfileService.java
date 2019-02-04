package org.folio.services;

import io.vertx.core.Future;
import org.folio.dao.ProfileDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Generic implementation of the {@link ProfileService}
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public abstract class AbstractProfileService<T, S> implements ProfileService<T, S> {

  @Autowired
  protected ProfileDao<T, S> profileDao;

  @Override
  public Future<S> getProfiles(String query, int offset, int limit, String tenantId) {
    return profileDao.getProfiles(query, offset, limit, tenantId);
  }

  @Override
  public Future<Optional<T>> getProfileById(String id, String tenantId) {
    return profileDao.getProfileById(id, tenantId);
  }

  @Override
  public Future<T> saveProfile(T profile, String tenantId) {
    return profileDao.saveProfile(setProfileId(profile), tenantId).map(profile);  }

  @Override
  public Future<T> updateProfile(T profile, String tenantId) {
    return profileDao.updateProfile(profile, tenantId);
  }

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    return profileDao.deleteProfile(id, tenantId);
  }

  /**
   * Generates id and sets it to the Profile entity
   *
   * @param profile Profile
   * @return Profile with generated id
   */
  abstract T setProfileId(T profile);

}
