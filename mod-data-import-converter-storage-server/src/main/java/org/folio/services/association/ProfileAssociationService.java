package org.folio.services.association;

import java.util.Optional;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;

/**
 * Generic Profile Association Service
 */
public interface ProfileAssociationService { //NOSONAR

  /**
   * Searches for ProfileAssociation by id
   *
   * @param id       entity id
   * @param tenantId tenant id
   * @return future with optional {@link ProfileAssociation}
   */
  Future<Optional<ProfileAssociation>> getById(String id, String tenantId);

  /**
   * Saves ProfileAssociation entity
   *
   * @param entity ProfileAssociation to save
   * @param params {@link OkapiConnectionParams}
   * @return future with saved entity
   */
  Future<ProfileAssociation> save(ProfileAssociation entity, OkapiConnectionParams params);

  /**
   * Updates ProfileAssociation with given id
   *
   * @param entity ProfileAssociation to update
   * @param params {@link OkapiConnectionParams}
   * @return future with updated entity
   */
  Future<ProfileAssociation> update(ProfileAssociation entity, OkapiConnectionParams params);

  /**
   * Deletes ProfileAssociation entity by id
   *
   * @param id       entity id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> delete(String id, String tenantId);

  /**
   * Finds details by master id.
   *
   * @param tenantId   a tenant id
   * @param masterId   a master id
   * @param masterType a master type
   * @return list of details for specified master
   */
  Future<Optional<ProfileSnapshotWrapper>> findDetails(String tenantId, String masterId, ContentType masterType);

  /**
   * Finds masters by detail id.
   *
   * @param tenantId   a tenant id
   * @param detailId   a detail id
   * @param detailType a detail type
   * @return list of masters profiles for specified detail profile
   */
  Future<Optional<ProfileSnapshotWrapper>> findMasters(String tenantId, String detailId, ContentType detailType);
}
