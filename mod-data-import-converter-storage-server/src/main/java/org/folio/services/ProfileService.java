package org.folio.services;

import io.vertx.core.Future;

import java.util.Optional;

/**
 *  Generic Profile Service
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public interface ProfileService<T, S> {

  /**
   * Searches for T entities
   *
   * @param query  query from URL
   * @param offset starting index in a list of results
   * @param limit  limit of records for pagination
   * @param tenantId tenant id
   * @return future with S, a collection of T entities
   */
  Future<S> getProfiles(String query, int offset, int limit, String tenantId);

  /**
   * Searches for T by id
   *
   * @param id Profile id
   * @param tenantId tenant id
   * @return future with optional {@link T}
   */
  Future<Optional<T>> getProfileById(String id, String tenantId);

  /**
   * Saves T entity
   *
   * @param profile Profile to save
   * @param tenantId tenant id
   * @return future with saved entity
   */
  Future<T> saveProfile(T profile, String tenantId);

  /**
   * Updates T with given id
   *
   * @param profile Profile to update
   * @param tenantId tenant id
   * @return future with updated entity
   */
  Future<T> updateProfile(T profile, String tenantId);

  /**
   * Deletes T entity by id
   *
   * @param id Profile id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> deleteProfile(String id, String tenantId);
}
