package org.folio.services.snapshot;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

import java.util.Optional;

/**
 *  Profile snapshot service
 */
public interface ProfileSnapshotService {

  /**
   * Searches for ProfileSnapshotWrapper by id
   *
   * @param id       ProfileSnapshotWrapper id
   * @param tenantId tenant id
   * @return future with optional {@link ProfileSnapshotWrapper}
   */
  Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId);
}
