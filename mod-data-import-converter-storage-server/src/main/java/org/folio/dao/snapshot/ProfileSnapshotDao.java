package org.folio.dao.snapshot;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

import java.util.Optional;

/**
 * Profile snapshot DAO
 */
public interface ProfileSnapshotDao {
  /**
   * Searches for ProfileSnapshotWrapper by id
   *
   * @param id       ProfileSnapshotWrapper id
   * @param tenantId tenant id
   * @return future with optional {@link ProfileSnapshotWrapper}
   */
  Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId);

}
