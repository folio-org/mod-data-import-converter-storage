package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.impl.util.ExceptionHelper;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSetting;
import org.folio.rest.jaxrs.resource.FieldProtectionSettings;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.services.fieldprotectionsettings.MarcFieldProtectionSettingsService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;

import static java.lang.String.format;

public class FieldProtectionSettingsImpl implements FieldProtectionSettings {

  private static final Logger LOGGER = LogManager.getLogger();

  @Autowired
  private MarcFieldProtectionSettingsService marcFieldProtectionSettingsService;
  private final String tenantId;

  public FieldProtectionSettingsImpl(Vertx vertx, String tenantId) { //NOSONAR
    SpringContextUtil.autowireDependencies(this, Vertx.currentContext());
    this.tenantId = TenantTool.calculateTenantId(tenantId);
  }

  @Override
  public void getFieldProtectionSettingsMarc(String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      marcFieldProtectionSettingsService.getMarcFieldProtectionSettings(query, offset, limit, tenantId)
        .map(FieldProtectionSettings.GetFieldProtectionSettingsMarcResponse::respond200WithApplicationJson)
        .map(Response.class::cast)
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      LOGGER.warn("getFieldProtectionSettingsMarc:: Failed to get all MARC field protection settings", e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }

  @Override
  public void postFieldProtectionSettingsMarc(String lang, MarcFieldProtectionSetting entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      marcFieldProtectionSettingsService.addMarcFieldProtectionSetting(entity, tenantId)
        .map(setting -> (Response) FieldProtectionSettings.PostFieldProtectionSettingsMarcResponse.respond201WithApplicationJson(setting, FieldProtectionSettings.PostFieldProtectionSettingsMarcResponse.headersFor201()))
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      LOGGER.warn("postFieldProtectionSettingsMarc:: Failed to save MARC field protection setting", e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }

  @Override
  public void putFieldProtectionSettingsMarcById(String id, String lang, MarcFieldProtectionSetting entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      entity.setId(id);
      marcFieldProtectionSettingsService.updateMarcFieldProtectionSetting(entity, tenantId)
        .map(setting -> (Response) FieldProtectionSettings.PutFieldProtectionSettingsMarcByIdResponse.respond200WithApplicationJson(setting))
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      LOGGER.warn("putFieldProtectionSettingsMarcById:: Failed to update MARC field protection setting by id {}", id, e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }

  @Override
  public void deleteFieldProtectionSettingsMarcById(String id, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      marcFieldProtectionSettingsService.deleteMarcFieldProtectionSetting(id, tenantId)
        .map(deleted -> Boolean.TRUE.equals(deleted)
          ? FieldProtectionSettings.DeleteFieldProtectionSettingsMarcByIdResponse.respond204()
          : FieldProtectionSettings.DeleteFieldProtectionSettingsMarcByIdResponse.respond404WithTextPlain(format("MARC field protection setting with id '%s' was not found", id)))
        .map(Response.class::cast)
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      LOGGER.warn("deleteFieldProtectionSettingsMarcById:: Failed to delete MARC field protection setting by id {}", id, e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }

  @Override
  public void getFieldProtectionSettingsMarcById(String id, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      marcFieldProtectionSettingsService.getMarcFieldProtectionSettingById(id, tenantId)
        .map(optionalSetting -> optionalSetting.orElseThrow(() ->
          new NotFoundException(String.format("MARC field protection setting with id '%s' was not found", id))))
        .map(GetFieldProtectionSettingsMarcByIdResponse::respond200WithApplicationJson)
        .map(Response.class::cast)
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      LOGGER.warn("getFieldProtectionSettingsMarcById:: Failed to get MARC field protection setting by id {}", id, e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }
}
