package org.folio.dao.snapshot;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

import java.util.List;
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

  /**
   * Saves ProfileSnapshotWrapper entity to database
   *
   * @param entity   ProfileSnapshotWrapper to save
   * @param tenantId tenant id
   * @return future
   */
  Future<String> save(ProfileSnapshotWrapper entity, String tenantId);

  /**
   * Returns the list of snapshot items, listed in hierarchical order
   *
   * @param profileId    profile uuid
   * @param profileType  profile type
   * @param jobProfileId job profile uuid
   * @param tenantId     tenant id
   * @return list of the snapshot items
   */
  Future<List<ProfileSnapshotItem>> getSnapshotItems(String profileId, ProfileSnapshotWrapper.ContentType profileType, String jobProfileId, String tenantId);
}
