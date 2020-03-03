package org.folio.services.forms.configs;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.FormConfig;
import org.folio.rest.jaxrs.model.FormConfigCollection;

import java.util.Optional;

/**
 * FormConfig service interface
 */
public interface FormConfigService {

  /**
   * Saves formConfig
   *
   * @param formConfig entity to save
   * @param tenantId   tenant id
   * @return future with formConfig
   */
  Future<FormConfig> save(FormConfig formConfig, String tenantId);

  /**
   * Searches for all formConfig entities
   *
   * @param tenantId tenant id
   * @return future with {@link FormConfigCollection}
   */
  Future<FormConfigCollection> getAll(String tenantId);

  /**
   * Searches for formConfig by formName
   *
   * @param formName form name
   * @param tenantId tenant id
   * @return future with {@link Optional} describing FormConfig
   */
  Future<FormConfig> getByFormName(String formName, String tenantId);

  /**
   * Updates formConfig entity by formName in database
   *
   * @param formConfig entity to update
   * @param tenantId   tenant id
   * @return future with updated formConfig
   */
  Future<FormConfig> update(FormConfig formConfig, String tenantId);

  /**
   * Deletes formConfig entity by formName in database
   *
   * @param formName form name
   * @param tenantId tenant id
   * @return future with true if the formConfig was deleted, otherwise future with false
   */
  Future<Boolean> deleteByFormName(String formName, String tenantId);
}
