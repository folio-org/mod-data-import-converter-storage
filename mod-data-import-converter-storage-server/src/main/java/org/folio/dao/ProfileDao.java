package org.folio.dao;

import io.vertx.core.Future;

import java.util.Optional;

/**
 * Generic data access object
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public interface ProfileDao<T, S> {

  /**
   * Searches for T entities in database
   *
   * @param showDeleted indicates to return T entities marked as deleted or not
   * @param query    query from URL
   * @param offset   starting index in a list of results
   * @param limit    limit of records for pagination
   * @param tenantId tenant id
   * @return future with S, a collection of T entities
   */
  Future<S> getProfiles(boolean showDeleted, String query, int offset, int limit, String tenantId);

  /**
   * Searches for T entity by id
   *
   * @param id       Profile id
   * @param tenantId tenant id
   * @return future with optional entity
   */
  Future<Optional<T>> getProfileById(String id, String tenantId);

  /**
   * Saves T entity to database
   *
   * @param profile  Profile to save
   * @param tenantId tenant id
   * @return future
   */
  Future<String> saveProfile(T profile, String tenantId);

  /**
   * Updates T entity in database
   *
   * @param profile  Profile to update
   * @param tenantId tenant id
   * @return future with updated entity
   */
  Future<T> updateProfile(T profile, String tenantId);

  /**
   * Search in database profile with the same name
   *
   * @param profileName - profile name
   * @param profileId - profile name
   * @param tenantId    - tenant id from request
   * @return - boolean value. True if job profile with the same name already exist
   */
  Future<Boolean> isProfileExistByName(String profileName, String profileId, String tenantId);
}
