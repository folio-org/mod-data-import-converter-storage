package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dataImport.util.ExceptionHelper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.resource.DataImportProfiles;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.services.ProfileService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;

public class DataImportProfilesImpl implements DataImportProfiles {

  private static final Logger logger = LoggerFactory.getLogger(DataImportProfilesImpl.class);

  @Autowired
  private ProfileService<JobProfile, JobProfileCollection> jobProfileService;

  private String tenantId;

  public DataImportProfilesImpl(Vertx vertx, String tenantId) {
    SpringContextUtil.autowireDependencies(this, Vertx.currentContext());
    this.tenantId = TenantTool.calculateTenantId(tenantId);
  }

  @Override
  public void postDataImportProfilesJobProfiles(String lang, JobProfile entity, Map<String, String> okapiHeaders,
                                                Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        jobProfileService.saveProfile(entity, tenantId)
          .map((Response) PostDataImportProfilesJobProfilesResponse
            .respond201WithApplicationJson(entity, PostDataImportProfilesJobProfilesResponse.headersFor201()))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .setHandler(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to create Job Profile", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesJobProfiles(String query, int offset, int limit, String lang, Map<String, String> okapiHeaders,
                                               Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        jobProfileService.getProfiles(query, offset, limit, tenantId)
          .map(GetDataImportProfilesJobProfilesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .setHandler(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Job Profiles");
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void putDataImportProfilesJobProfilesById(String id, String lang, JobProfile entity, Map<String, String> okapiHeaders,
                                                   Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.setId(id);
        jobProfileService.updateProfile(entity, tenantId)
          .map(updatedEntity -> (Response) PutDataImportProfilesJobProfilesByIdResponse.respond200WithApplicationJson(updatedEntity))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .setHandler(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to update Job Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesJobProfilesById(String id, String lang, Map<String, String> okapiHeaders,
                                                   Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        jobProfileService.getProfileById(id, tenantId)
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(String.format("Job Profile with id '%s' was not found", id))))
          .map(GetDataImportProfilesJobProfilesByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .setHandler(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Job Profile by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void deleteDataImportProfilesJobProfilesById(String id, String lang, Map<String, String> okapiHeaders,
                                                      Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        jobProfileService.deleteProfile(id, tenantId)
          .map(deleted -> deleted ?
            DeleteDataImportProfilesJobProfilesByIdResponse.respond204WithTextPlain(
              String.format("Job Profile with id '%s' was successfully deleted", id)) :
            DeleteDataImportProfilesJobProfilesByIdResponse.respond404WithTextPlain(
              String.format("Job Profile with id '%s' was not found", id)))
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .setHandler(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to delete Job Profile with id {}",id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }
}
