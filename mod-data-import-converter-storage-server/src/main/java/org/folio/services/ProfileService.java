package org.folio.services;

import io.vertx.core.Future;

import java.util.Optional;

/**
 *  Generic Profile Service
 */
public interface ProfileService <T, R> {

  /**
   * Searches for T entities
   *
   * @param query  query from URL
   * @param offset starting index in a list of results
   * @param limit  limit of records for pagination
   * @return future with R, a collection of T entities
   */
  Future<R> getProfiles(String query, int offset, int limit);

  /**
   * Searches for T by id
   *
   * @param id Profile id
   * @return future with optional {@link T}
   */
  Future<Optional<T>> getProfileById(String id);

  /**
   * Saves T entity
   *
   * @param profile Profile to save
   * @return future with saved entity
   */
  Future<T> saveProfile(T profile);

  /**
   * Updates T with given id
   *
   * @param profile Profile to update
   * @return future with updated entity
   */
  Future<T> updateProfile(T profile);

  /**
   * Deletes T entity by id
   *
   * @param id Profile id
   * @return future with true if succeeded
   */
  Future<Boolean> deleteProfile(String id);
}
