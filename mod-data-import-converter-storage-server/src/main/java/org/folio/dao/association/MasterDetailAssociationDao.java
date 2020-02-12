package org.folio.dao.association;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

import java.util.List;

/**
 * This DAO is for profiles which are associated with other in 'master' to 'detail' relationship
 */
public interface MasterDetailAssociationDao {

  /**
   * Returns 'detail' profiles linked to 'master' profile id
   *
   * @param masterId   'master' profile id
   * @param detailType detail type that should be find
   * @param query      a cql query for a detail
   * @param offset     an offset
   * @param limit      a limit
   * @param tenantId   tenant id
   * @return a list of details for a master id
   */
  Future<List<ProfileSnapshotWrapper>> getDetailProfilesByMasterId(String masterId, ProfileSnapshotWrapper.ContentType detailType, String query, int offset, int limit, String tenantId);

  /**
   * Returns master profiles linked to detail profile id
   *
   * @param detailId   detail profile id
   * @param masterType a master type that should be find
   * @param query      a cql query for a master
   * @param offset     an offset
   * @param limit      a limit
   * @param tenantId   tenant id
   * @return a list of masters for a detail id
   */
  Future<List<ProfileSnapshotWrapper>> getMasterProfilesByDetailId(String detailId, ProfileSnapshotWrapper.ContentType masterType, String query, int offset, int limit, String tenantId);
}
