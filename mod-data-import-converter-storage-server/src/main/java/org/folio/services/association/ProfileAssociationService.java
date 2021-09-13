package org.folio.services.association;

import io.vertx.core.Future;
import org.folio.rest.impl.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileAssociationCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;

import java.util.Optional;

/**
 * Generic Profile Association Service
 */
public interface ProfileAssociationService { //NOSONAR

  /**
   * Searches for ProfileAssociation by masterType and detailType
   *
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @param tenantId   tenant id
   * @return future with {@link ProfileAssociationCollection}
   */
  Future<ProfileAssociationCollection> getAll(ContentType masterType, ContentType detailType, String tenantId);

  /**
   * Searches for ProfileAssociation by id
   *
   * @param id         entity id
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @param tenantId   tenant id
   * @return future with optional {@link ProfileAssociation}
   */
  Future<Optional<ProfileAssociation>> getById(String id, ContentType masterType, ContentType detailType, String tenantId);

  /**
   * Saves ProfileAssociation entity
   *
   * @param entity     ProfileAssociation to save
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @param tenantId   tenantId
   * @return future with saved entity
   */
  Future<ProfileAssociation> save(ProfileAssociation entity, ContentType masterType, ContentType detailType, String tenantId);

  /**
   * Updates ProfileAssociation with given id
   *
   * @param entity     ProfileAssociation to update
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @param params     {@link OkapiConnectionParams}
   * @return future with updated entity
   */
  Future<ProfileAssociation> update(ProfileAssociation entity, ContentType masterType, ContentType detailType, OkapiConnectionParams params);

  /**
   * Deletes ProfileAssociation entity by id
   *
   * @param id         entity id
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @param tenantId   tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> delete(String id, ContentType masterType, ContentType detailType, String tenantId);

  /**
   * Finds details by master id.
   *
   * @param masterId   a master id
   * @param masterType a master type
   * @param detailType detail type that should be find
   * @param query      a cql query for a detail
   * @param offset     an offset
   * @param limit      a limit
   * @param tenantId   a tenant id
   * @return list of details for specified master
   */
  Future<Optional<ProfileSnapshotWrapper>> findDetails(String masterId, ContentType masterType, ContentType detailType, String query, int offset, int limit, String tenantId);

  /**
   * Finds masters by detail id.
   *
   * @param detailId   a detail id
   * @param detailType a detail type
   * @param masterType a master type that should be find
   * @param query      a cql query for a master
   * @param offset     an offset
   * @param limit      a limit
   * @param tenantId   a tenant id
   * @return list of masters profiles for specified detail profile
   */
  Future<Optional<ProfileSnapshotWrapper>> findMasters(String detailId, ContentType detailType, ContentType masterType, String query, int offset, int limit, String tenantId);

  /**
   * Delete ProfileAssociation by masterId and detailId
   *
   * @param masterId     - UUID of masterProfile
   * @param detailId     - UUID of detailProfile
   * @param masterType   - master Profile Type
   * @param detailType   - detail Profile Type
   * @param tenantId     - tenant id
   * @param jobProfileId - job profile id (optional)
   * @return - boolean result of operation
   */
  Future<Boolean> delete(String masterId, String detailId, ContentType masterType, ContentType detailType, String tenantId, String jobProfileId);

  /**
   * Delete profile associations for particular master profile by masterId
   *
   * @param masterId   - master profile id
   * @param masterType - master profile type
   * @param detailType - detail profile type
   * @param tenantId   - tenant id
   * @return future with boolean
   */
  Future<Boolean> deleteByMasterId(String masterId, ContentType masterType, ContentType detailType, String tenantId);
}
