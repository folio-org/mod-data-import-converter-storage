package org.folio.dao.fieldprotectionsettings;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSetting;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSettingsCollection;

import java.util.Optional;

/**
 * Data access object for {@link MarcFieldProtectionSetting}
 */
public interface MarcFieldProtectionSettingsDao {

  /**
   * Searches for {@link MarcFieldProtectionSetting} in database
   *
   * @param query    query from URL
   * @param offset   starting index in a list of results
   * @param limit    limit of records for pagination
   * @param tenantId tenant id tenant id
   * @return future with {@link MarcFieldProtectionSettingsCollection}
   */
  Future<MarcFieldProtectionSettingsCollection> getAll(String query, int offset, int limit, String tenantId);

  /**
   * Searches for {@link MarcFieldProtectionSetting} by id
   *
   * @param id       MarcFieldProtectionSetting id
   * @param tenantId tenant id tenant id
   * @return future with optional {@link MarcFieldProtectionSetting}
   */
  Future<Optional<MarcFieldProtectionSetting>> getById(String id, String tenantId);

  /**
   * Saves {@link MarcFieldProtectionSetting} to database
   *
   * @param marcFieldProtectionSetting MarcFieldProtectionSetting to save
   * @param tenantId      tenant id tenant id
   * @return future
   */
  Future<String> save(MarcFieldProtectionSetting marcFieldProtectionSetting, String tenantId);

  /**
   * Updates {@link MarcFieldProtectionSetting} in database
   *
   * @param marcFieldProtectionSetting MarcFieldProtectionSetting to update
   * @param tenantId      tenant id
   * @return future with {@link MarcFieldProtectionSetting}
   */
  Future<MarcFieldProtectionSetting> update(MarcFieldProtectionSetting marcFieldProtectionSetting, String tenantId);

  /**
   * Deletes {@link MarcFieldProtectionSetting} from database
   *
   * @param id       MarcFieldProtectionSetting id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> delete(String id, String tenantId);
}
