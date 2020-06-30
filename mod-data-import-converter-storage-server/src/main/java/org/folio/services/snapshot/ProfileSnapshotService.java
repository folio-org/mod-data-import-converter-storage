package org.folio.services.snapshot;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

import java.util.Optional;

/**
 * Profile snapshot service
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

  /**
   * Creates and saves snapshot for the given Job Profile
   *
   * @param jobProfileId job profile id
   * @param tenantId     tenant id
   * @return future with snapshot {@link ProfileSnapshotWrapper}
   */
  Future<ProfileSnapshotWrapper> createSnapshot(String jobProfileId, String tenantId);

  /**
   * Constructs a snapshot wrapper for a specified Profile without saving it in the db
   *
   * @param profileId    profile id
   * @param profileType  profile type
   * @param jobProfileId job profile id
   * @param tenantId     tenant id
   * @return future with snapshot {@link ProfileSnapshotWrapper}
   */
  Future<ProfileSnapshotWrapper> constructSnapshot(String profileId, ProfileSnapshotWrapper.ContentType profileType, String jobProfileId, String tenantId);
}
