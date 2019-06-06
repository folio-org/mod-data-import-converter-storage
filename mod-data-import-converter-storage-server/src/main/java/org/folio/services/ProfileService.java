package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.EntityTypeCollection;

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
   * @param showDeleted indicates to return T entities marked as deleted or not
   * @param query  query from URL
   * @param offset starting index in a list of results
   * @param limit  limit of records for pagination
   * @param tenantId tenant id
   * @return future with S, a collection of T entities
   */
  Future<S> getProfiles(boolean showDeleted, String query, int offset, int limit, String tenantId);

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
   * @param params {@link OkapiConnectionParams}
   * @return future with saved entity
   */
  Future<T> saveProfile(T profile, OkapiConnectionParams params);

  /**
   * Updates T with given id
   *
   * @param profile Profile to update
   * @param params {@link OkapiConnectionParams}
   * @return future with updated entity
   */
  Future<T> updateProfile(T profile, OkapiConnectionParams params);

  /**
   * Search in database profile with the same name which contains in specified profile
   *
   *
   * @param profile - T entity
   * @param tenantId    - tenant id from request
   * @return - boolean value. True if job profile with the same name already exist
   */
  Future<Boolean> isProfileExistByProfileName(T profile, String tenantId);

  /**
   * Updates the flag deleted to true in T entity
   *
   * @param id       Profile id
   * @param tenantId tenant id from request
   * @return future with true if succeeded
   */
  Future<Boolean> markProfileAsDeleted(String id, String tenantId);

  /**
   * Returns {@link EntityTypeCollection}
   *
   * @return future with {@link EntityTypeCollection}
   */
  Future<EntityTypeCollection> getEntityTypes();

  /**
   * Search in database associations where profile associated as detail by its id
   *
   * @param profileId - profile id
   * @param tenantId - tenant id
   * @return - future with boolean value.
   * True if profile with specified profileId associated as detail with other profiles
   */
  Future<Boolean> isProfileAssociatedAsDetail(String profileId, String tenantId);
}
