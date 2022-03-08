package org.folio.services;

import io.vertx.core.Future;
import org.folio.rest.impl.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.EntityTypeCollection;

import java.util.Optional;

/**
 * Generic Profile Service
 *
 * @param <T> type of the entity
 * @param <S> type of the collection of T entities
 */
public interface ProfileService<T, S, D> {

  /**
   * Searches for T entities
   *
   * @param showDeleted   indicates to return T entities marked as deleted or not
   * @param showHidden    indicates to return T entities marked as hidden or not
   * @param query         query from URL
   * @param offset        starting index in a list of results
   * @param limit         limit of records for pagination
   * @param withRelations load profile with related profiles
   * @param tenantId      tenant id
   * @return future with S, a collection of T entities
   */
  Future<S> getProfiles(boolean showDeleted, boolean withRelations, boolean showHidden, String query, int offset, int limit, String tenantId);

  /**
   * Searches for T by id
   *
   * @param id            Profile id
   * @param tenantId      tenant id
   * @param withRelations load profile with related profiles
   * @return future with optional {@link T}
   */
  Future<Optional<T>> getProfileById(String id, boolean withRelations, String tenantId);

  /**
   * Saves T entity
   *
   * @param profile Profile to save
   * @param params  {@link OkapiConnectionParams}
   * @return future with saved entity
   */
  Future<T> saveProfile(D profile, OkapiConnectionParams params);

  /**
   * Updates T with given id
   *
   * @param profile Profile to update
   * @param params  {@link OkapiConnectionParams}
   * @return future with updated entity
   */
  Future<T> updateProfile(D profile, OkapiConnectionParams params);

  /**
   * Search in database profile with the same name which contains in specified profile
   *
   * @param profile  - T entity
   * @param tenantId - tenant id from request
   * @return - boolean value. True if job profile with the same name already exist
   */
  Future<Boolean> isProfileExistByProfileName(T profile, String tenantId);

  /**
   * Search in database profile with the same id which contains in specified profile
   *
   * @param profile  - T entity
   * @param tenantId - tenant id from request
   * @return - boolean value. True if job profile with the same id already exist
   */
  Future<Boolean> isProfileExistByProfileId(T profile, String tenantId);

  /**
   * Check profile to access another fields except tags
   *
   * @param id               - Profile id
   * @param profile          - D DTO
   * @param isDefaultProfile - True if the profile lists as default
   * @param tenantId         - Tenant id from request
   * @return - boolean value. True if in the profile DTO has been changed only Tags
   */
  Future<Boolean> isProfileDtoValidForUpdate(String id, D profile, boolean isDefaultProfile, String tenantId);

  /**
   * Marks profile as deleted by its id
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
}
