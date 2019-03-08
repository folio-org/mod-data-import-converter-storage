package org.folio.dao.association;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.PostgresClientFactory;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Generic implementation of the of the {@link ProfileAssociationDao}
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
public abstract class AbstractProfileAssociationDao<M, D> implements ProfileAssociationDao<M, D> {

  private static final Logger logger = LoggerFactory.getLogger(JobToMatchProfileAssociationDaoImpl.class);
  private static final String ID_FIELD = "'id'";

  @Autowired
  protected PostgresClientFactory pgClientFactory;

  private ProfileDao masterProfileDao;
  private ProfileDao detailProfileDao;

  public AbstractProfileAssociationDao(ProfileDao masterProfileDao, ProfileDao detailProfileDao) {
    this.masterProfileDao = masterProfileDao;
    this.detailProfileDao = detailProfileDao;
  }

  @Override
  public Future<String> save(ProfileAssociation entity, String tenantId) {
    // STUB implementation
    return Future.succeededFuture();
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, String tenantId) {
    // STUB implementation
    return Future.succeededFuture(Optional.of(null));
  }

  @Override
  public Future<D> getDetailProfilesByMasterId(String masterId) {
    // STUB implementation, use detailProfileDao to obtain "detail" profiles
    return Future.succeededFuture();
  }

  @Override
  public Future<M> getMasterProfilesByDetailId(String detailId) {
    // STUB implementation, use masterProfileDao to obtain "master" profiles
    return Future.succeededFuture();
  }

  /**
   * Provides access to the table name
   *
   * @return table name
   */
  abstract String getTableName();
}
