package org.folio.dao.forms.configs;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.FormConfig;
import org.folio.rest.jaxrs.model.FormConfigCollection;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.folio.dao.util.DaoUtil.constructCriteria;


@Repository
public class FormConfigDaoImpl implements FormConfigDao {

  public static final String TABLE_NAME = "forms_configs";
  private static final String FORM_NAME_FIELD = "'formName'";

  @Autowired
  private PostgresClientFactory pgClientFactory;

  @Override
  public Future<FormConfig> save(FormConfig formConfig, String tenantId) {
    Promise<String> promise = Promise.promise();
    formConfig.withId(UUID.randomUUID().toString());
    pgClientFactory.createInstance(tenantId).save(TABLE_NAME, formConfig.getId(), formConfig, promise);
    return promise.future().map(formConfig);
  }

  @Override
  public Future<FormConfigCollection> getAll(String tenantId) {
    Promise<Results<FormConfig>> promise = Promise.promise();
    pgClientFactory.createInstance(tenantId).get(TABLE_NAME, FormConfig.class, new Criterion(new Criteria()), true, false, promise);
    return promise.future().map(results -> new FormConfigCollection()
      .withFormConfigs(results.getResults())
      .withTotalRecords(results.getResults().size()));
  }

  @Override
  public Future<Optional<FormConfig>> getByFormName(String formName, String tenantId) {
    Promise<Results<FormConfig>> promise = Promise.promise();
    Criteria formIdCriteria = constructCriteria(FORM_NAME_FIELD, formName);
    pgClientFactory.createInstance(tenantId).get(TABLE_NAME, FormConfig.class, new Criterion(formIdCriteria), true, false, promise);
    return promise.future()
      .map(Results::getResults)
      .map(configsList -> configsList.isEmpty() ? Optional.empty() : Optional.of(configsList.get(0)));
  }

  @Override
  public Future<FormConfig> updateByFormName(FormConfig formConfig, String tenantId) {
    Promise<RowSet<Row>> promise = Promise.promise();
    Criteria formIdCriteria = constructCriteria(FORM_NAME_FIELD, formConfig.getFormName());
    pgClientFactory.createInstance(tenantId).update(TABLE_NAME, formConfig, new Criterion(formIdCriteria), true, promise);
    return promise.future().compose(updateResult -> updateResult.rowCount() == 1
      ? Future.succeededFuture(formConfig)
      : Future.failedFuture(new NotFoundException(String.format("FormConfig with formName '%s' was not found", formConfig.getFormName()))));
  }

  @Override
  public Future<Boolean> deleteByFormName(String formName, String tenantId) {
    Promise<RowSet<Row>> promise = Promise.promise();
    Criteria formIdCriteria = constructCriteria(FORM_NAME_FIELD, formName);
    pgClientFactory.createInstance(tenantId).delete(TABLE_NAME, new Criterion(formIdCriteria), promise);
    return promise.future().map(updateResult -> updateResult.rowCount() == 1);
  }
}
