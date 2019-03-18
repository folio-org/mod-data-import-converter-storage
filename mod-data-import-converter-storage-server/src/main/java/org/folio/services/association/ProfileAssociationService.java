package org.folio.services.association;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ProfileAssociation;

import java.util.Optional;

/**
 * Generic Profile Association Service
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
public interface ProfileAssociationService<M, D> { //NOSONAR

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
}
