package org.folio.services.fieldprotectionsettings;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSetting;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSettingsCollection;

import java.util.Optional;

/**
 * MARC Field Protection Settings Service
 */
public interface MarcFieldProtectionSettingsService {

  /**
   * Searches for {@link MarcFieldProtectionSetting}
   *
   * @param query    query from URL
   * @param offset   starting index in a list of results
   * @param limit    limit of records for pagination
   * @param tenantId tenant id
   * @return future with {@link MarcFieldProtectionSettingsCollection}
   */
  Future<MarcFieldProtectionSettingsCollection> getMarcFieldProtectionSettings(String query, int offset, int limit, String tenantId);

  /**
   * Searches for {@link MarcFieldProtectionSetting} by id
   *
   * @param id       MarcFieldProtectionSetting id
   * @param tenantId tenant id
   * @return future with optional {@link MarcFieldProtectionSetting}
   */
  Future<Optional<MarcFieldProtectionSetting>> getMarcFieldProtectionSettingById(String id, String tenantId);

  /**
   * Saves {@link MarcFieldProtectionSetting}
   *
   * @param marcFieldProtectionSetting MarcFieldProtectionSetting to save
   * @param tenantId        tenant id
   * @return future with {@link MarcFieldProtectionSetting}
   */
  Future<MarcFieldProtectionSetting> addMarcFieldProtectionSetting(MarcFieldProtectionSetting marcFieldProtectionSetting, String tenantId);

  /**
   * Updates {@link MarcFieldProtectionSetting} with given id
   *
   * @param marcFieldProtectionSetting MarcFieldProtectionSetting to update
   * @param tenantId        tenant id
   * @return future with {@link MarcFieldProtectionSetting}
   */
  Future<MarcFieldProtectionSetting> updateMarcFieldProtectionSetting(MarcFieldProtectionSetting marcFieldProtectionSetting, String tenantId);

  /**
   * Deletes {@link MarcFieldProtectionSetting} by id
   *
   * @param id       MarcFieldProtectionSetting id
   * @param tenantId tenant id
   * @return future with true if succeeded
   */
  Future<Boolean> deleteMarcFieldProtectionSetting(String id, String tenantId);
}
