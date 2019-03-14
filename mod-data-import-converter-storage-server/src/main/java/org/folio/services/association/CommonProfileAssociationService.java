package org.folio.services.association;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.association.ProfileAssociationDao;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Generic implementation of the {@link ProfileAssociationService}
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
@Service
public class CommonProfileAssociationService<M, D> implements ProfileAssociationService<M, D> { //NOSOANR

  @Autowired
  private ProfileAssociationDao<M, D> dao;

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, String tenantId) {
    return dao.getById(id, tenantId);
  }

  @Override
  public Future<ProfileAssociation> save(ProfileAssociation entity, OkapiConnectionParams params) {
    entity.setId(UUID.randomUUID().toString());
    return dao.save(entity, params.getTenantId()).map(entity);
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, OkapiConnectionParams params) {
    return dao.update(entity, params.getTenantId());
  }

  @Override
  public Future<Boolean> delete(String id, String tenantId) {
    return dao.delete(id, tenantId);
  }
}
