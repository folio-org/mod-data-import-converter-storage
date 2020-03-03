package org.folio.services.forms.configs;

import io.vertx.core.Future;
import org.folio.dao.forms.configs.FormConfigDao;
import org.folio.rest.jaxrs.model.FormConfig;
import org.folio.rest.jaxrs.model.FormConfigCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;

import static java.lang.String.format;

@Component
public class FormConfigServiceImpl implements FormConfigService {

  @Autowired
  private FormConfigDao formConfigDao;

  @Override
  public Future<FormConfig> save(FormConfig formConfig, String tenantId) {
    return formConfigDao.save(formConfig, tenantId);
  }

  @Override
  public Future<FormConfig> getByFormName(String formName, String tenantId) {
    return formConfigDao.getByFormName(formName, tenantId)
      .compose(configOptional -> configOptional
        .map(Future::succeededFuture)
        .orElse(Future.failedFuture(new NotFoundException(format("FormConfig with formName '%s' was not found", formName)))));
  }

  @Override
  public Future<FormConfigCollection> getAll(String tenantId) {
    return formConfigDao.getAll(tenantId);
  }

  @Override
  public Future<FormConfig> update(FormConfig formConfig, String tenantId) {
    return formConfigDao.updateByFormName(formConfig, tenantId);
  }

  @Override
  public Future<Boolean> deleteByFormName(String formName, String tenantId) {
    return formConfigDao.deleteByFormName(formName, tenantId);
  }
}
