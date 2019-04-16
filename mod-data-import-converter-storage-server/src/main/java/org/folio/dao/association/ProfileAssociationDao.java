package org.folio.dao.association;

import java.util.List;
import java.util.Optional;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileAssociation;

/**
 * This DAO is for association between 2 profiles (which are called 'master' profile and 'detail' profile).
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
public interface ProfileAssociationDao<M, D> {

  /**
   * Saves ProfileAssociation entity to database
   *
   * @param entity   ProfileAssociation to save
   * @param tenantId tenant id
   * @return future
   */
  Future<String> save(ProfileAssociation entity, String tenantId);

  /**
   * Searches for ProfileAssociation entity by id
   *
   * @param id       ProfileAssociation id
   * @param tenantId tenant id
   * @return future with optional entity
   */
  Future<Optional<ProfileAssociation>> getById(String id, String tenantId);

  /**
   * Updates ProfileAssociation entity in database
   *
   * @param entity   ProfileAssociation entity to update
   * @param tenantId tenant id
   * @return future with updated entity
   */
  Future<ProfileAssociation> update(ProfileAssociation entity, String tenantId);

  /**
   * Deletes entity from database
   *
   * @param id       ProfileAssociation  id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> delete(String id, String tenantId);

  /**
   * Returns 'detail' profiles linked to 'master' profile id
   *
   * @param masterId 'master' profile id
   * @param tenantId tenant id
   * @return future
   */
  Future<List<ChildSnapshotWrapper>> getDetailProfilesByMasterId(String masterId, String tenantId);

  /**
   * Returns master profiles linked to detail profile id
   *
   * @param tenantId tenant id
   * @param detailId detail profile id
   * @return future
   */
  Future<List<ChildSnapshotWrapper>> getMasterProfilesByDetailId(String detailId, String tenantId);
}
