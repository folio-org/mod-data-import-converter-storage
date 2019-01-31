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
   * @param query  query from URL
   * @param offset starting index in a list of results
   * @param limit  limit of records for pagination
   * @return future with S, a collection of T entities
   */
  Future<S> getProfiles(String query, int offset, int limit);

  /**
   * Searches for T entity by id
   *
   * @param id Profile id
   * @return future with optional entity
   */
  Future<Optional<T>> getProfileById(String id);

  /**
   * Saves T entity to database
   *
   * @param profile Profile to save
   * @return future
   */
  Future<String> saveProfile(T profile);

  /**
   * Updates T entity in database
   *
   * @param profile Profile to update
   * @return future with updated entity
   */
  Future<T> updateProfile(T profile);

  /**
   * Deletes entity from database
   *
   * @param id Profile id
   * @return future with true if succeeded
   */
  Future<Boolean> deleteProfile(String id);
}
