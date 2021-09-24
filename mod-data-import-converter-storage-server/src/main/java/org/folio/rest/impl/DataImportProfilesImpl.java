package org.folio.rest.impl;

import static java.lang.String.format;

import static org.folio.rest.RestVerticle.OKAPI_HEADER_TOKEN;
import static org.folio.rest.RestVerticle.OKAPI_USERID_HEADER;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.folio.okapi.common.GenericCompositeFuture;
import org.folio.rest.impl.util.ExceptionHelper;
import org.folio.rest.impl.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.Error;
import org.folio.rest.jaxrs.model.Errors;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.JobProfileUpdateDto;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.MappingRule;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.jaxrs.model.MatchProfileUpdateDto;
import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.OperationType;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.jaxrs.resource.DataImportProfiles;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.services.ProfileService;
import org.folio.services.association.ProfileAssociationService;
import org.folio.services.snapshot.ProfileSnapshotService;
import org.folio.spring.SpringContextUtil;

public class DataImportProfilesImpl implements DataImportProfiles {

  private static final Logger logger = LogManager.getLogger();
  private static final String DUPLICATE_PROFILE_ERROR_CODE = "%s.duplication.invalid";
  private static final String DUPLICATE_PROFILE_ID_ERROR_CODE = "%s.duplication.id";
  private static final String PROFILE_VALIDATE_ERROR_MESSAGE = "Failed to validate %s";
  private static final String MASTER_PROFILE_NOT_FOUND_MSG = "Master profile with id '%s' was not found";
  private static final String DETAIL_PROFILE_NOT_FOUND_MSG = "Detail profile with id '%s' was not found";
  private static final String INVALID_REPEATABLE_FIELD_ACTION_FOR_EMPTY_SUBFIELDS_MESSAGE = "Invalid repeatableFieldAction for empty subfields: %s";

  private static final String OCLC_CREATE_INSTANCE_JOB_PROFILE_ID = "d0ebb7b0-2f0f-11eb-adc1-0242ac120002";
  private static final String OCLC_UPDATE_INSTANCE_JOB_PROFILE_ID = "91f9b8d6-d80e-4727-9783-73fb53e3c786";
  private static final String OCLC_MARC_MARC_MATCH_PROFILE_ID = "d27d71ce-8a1e-44c6-acea-96961b5592c6";
  private static final String OCLC_INSTANCE_UUID_MATCH_PROFILE_ID = "31dbb554-0826-48ec-a0a4-3c55293d4dee";
  private static final String OCLC_CREATE_MAPPING_PROFILE_ID = "d0ebbc2e-2f0f-11eb-adc1-0242ac120002";
  private static final String OCLC_UPDATE_MAPPING_PROFILE_ID = "862000b9-84ea-4cae-a223-5fc0552f2b42";
  private static final String OCLC_UPDATE_MARC_BIB_MAPPING_PROFILE_ID = "f90864ef-8030-480f-a43f-8cdd21233252";
  private static final String OCLC_CREATE_INSTANCE_ACTION_PROFILE_ID = "d0ebba8a-2f0f-11eb-adc1-0242ac120002";
  private static final String OCLC_UPDATE_INSTANCE_ACTION_PROFILE_ID = "cddff0e1-233c-47ba-8be5-553c632709d9";
  private static final String OCLC_UPDATE_MARC_BIB_ACTION_PROFILE_ID = "6aa8e98b-0d9f-41dd-b26f-15658d07eb52";
  private static final String DEFAULT_CREATE_DERIVE_HOLDINGS_JOB_PROFILE_ID = "fa0262c7-5816-48d0-b9b3-7b7a862a5bc7";
  private static final String DEFAULT_CREATE_DERIVE_HOLDINGS_MAPPING_PROFILE_ID = "e0fbaad5-10c0-40d5-9228-498b351dbbaa";
  private static final String DEFAULT_CREATE_DERIVE_HOLDINGS_ACTION_PROFILE_ID = "adbe1e5c-7796-4902-b18e-794b1d58caac";
  private static final String DEFAULT_CREATE_DERIVE_INSTANCE_JOB_PROFILE_ID = "6409dcff-71fa-433a-bc6a-e70ad38a9604";
  private static final String DEFAULT_CREATE_DERIVE_INSTANCE_MAPPING_PROFILE_ID = "991c0300-44a6-47e3-8ea2-b01bb56a38cc";
  private static final String DEFAULT_CREATE_DERIVE_INSTANCE_ACTION_PROFILE_ID = "f8e58651-f651-485d-aead-d2fa8700e2d1";


  @Autowired
  private ProfileService<JobProfile, JobProfileCollection, JobProfileUpdateDto> jobProfileService;
  @Autowired
  private ProfileService<MatchProfile, MatchProfileCollection, MatchProfileUpdateDto> matchProfileService;
  @Autowired
  private ProfileService<ActionProfile, ActionProfileCollection, ActionProfileUpdateDto> actionProfileService;
  @Autowired
  private ProfileService<MappingProfile, MappingProfileCollection, MappingProfileUpdateDto> mappingProfileService;
  @Autowired
  private ProfileAssociationService profileAssociationService;
  @Autowired
  private ProfileSnapshotService profileSnapshotService;

  private String tenantId;

  public DataImportProfilesImpl(Vertx vertx, String tenantId) { //NOSONAR
    SpringContextUtil.autowireDependencies(this, Vertx.currentContext());
    this.tenantId = TenantTool.calculateTenantId(tenantId);
  }

  @Override
  public void postDataImportProfilesJobProfiles(String lang, JobProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.getProfile().setMetadata(getMetadata(okapiHeaders));
        validateProfile(OperationType.CREATE, entity.getProfile(), jobProfileService, tenantId).onComplete(errors -> {
          if (errors.failed()) {
            logger.error(PROFILE_VALIDATE_ERROR_MESSAGE, errors.cause());
            asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
          } else if (errors.result().getTotalRecords() > 0) {
            asyncResultHandler.handle(Future.succeededFuture(PostDataImportProfilesJobProfilesResponse.respond422WithApplicationJson(errors.result())));
          } else {
            jobProfileService.saveProfile(entity, new OkapiConnectionParams(okapiHeaders))
              .map(profile -> (Response) PostDataImportProfilesJobProfilesResponse
                .respond201WithApplicationJson(entity.withProfile(profile).withId(profile.getId()), PostDataImportProfilesJobProfilesResponse.headersFor201()))
              .otherwise(ExceptionHelper::mapExceptionToResponse)
              .onComplete(asyncResultHandler);
          }
        });
      } catch (Exception e) {
        logger.error("Failed to create Job Profile", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesJobProfiles(boolean showDeleted, boolean withRelations, String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        jobProfileService.getProfiles(showDeleted, withRelations, query, offset, limit, tenantId)
          .map(GetDataImportProfilesJobProfilesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Job Profiles");
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void putDataImportProfilesJobProfilesById(String id, String lang, JobProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                   Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_INSTANCE_JOB_PROFILE_ID, OCLC_UPDATE_INSTANCE_JOB_PROFILE_ID)) {
          logger.error("Can`t update default OCLC Job Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t update default OCLC Job Profile with"))));
        } else {
          entity.getProfile().setMetadata(getMetadata(okapiHeaders));
          validateProfile(OperationType.UPDATE, entity.getProfile(), jobProfileService, tenantId).onComplete(errors -> {
            if (errors.failed()) {
              logger.error(PROFILE_VALIDATE_ERROR_MESSAGE, errors.cause());
              asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
            } else if (errors.result().getTotalRecords() > 0) {
              asyncResultHandler.handle(Future.succeededFuture(PutDataImportProfilesJobProfilesByIdResponse.respond422WithApplicationJson(errors.result())));
            } else {
              entity.getProfile().setId(id);
              jobProfileService.updateProfile(entity, new OkapiConnectionParams(okapiHeaders))
                .map(updatedEntity -> (Response) PutDataImportProfilesJobProfilesByIdResponse.respond200WithApplicationJson(updatedEntity))
                .otherwise(ExceptionHelper::mapExceptionToResponse)
                .onComplete(asyncResultHandler);
            }
          });
        }
      } catch (Exception e) {
        logger.error("Failed to update Job Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesJobProfilesById(String id, boolean withRelations, String lang, Map<String, String> okapiHeaders,
                                                   Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        jobProfileService.getProfileById(id, withRelations, tenantId)
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(format("Job Profile with id '%s' was not found", id))))
          .map(GetDataImportProfilesJobProfilesByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
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
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_INSTANCE_JOB_PROFILE_ID, OCLC_UPDATE_INSTANCE_JOB_PROFILE_ID, DEFAULT_CREATE_DERIVE_HOLDINGS_JOB_PROFILE_ID, DEFAULT_CREATE_DERIVE_INSTANCE_JOB_PROFILE_ID)) {
          logger.error("Can`t delete default OCLC Job Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t delete default OCLC Job Profile with"))));
        } else {
          OkapiConnectionParams params = new OkapiConnectionParams(okapiHeaders);
          jobProfileService.markProfileAsDeleted(id, params.getTenantId())
            .map(DeleteDataImportProfilesJobProfilesByIdResponse.respond204WithTextPlain(
              format("Job Profile with id '%s' was successfully deleted", id)))
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        }
      } catch (Exception e) {
        logger.error("Failed to delete Job Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void postDataImportProfilesMatchProfiles(String lang, MatchProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                  Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.getProfile().setMetadata(getMetadata(okapiHeaders));
        validateProfile(OperationType.CREATE, entity.getProfile(), matchProfileService, tenantId).onComplete(errors -> {
          if (errors.failed()) {
            logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
            asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
          } else if (errors.result().getTotalRecords() > 0) {
            asyncResultHandler.handle(Future.succeededFuture(PostDataImportProfilesMatchProfilesResponse.respond422WithApplicationJson(errors.result())));
          } else {
            matchProfileService.saveProfile(entity, new OkapiConnectionParams(okapiHeaders))
              .map(profile -> (Response) PostDataImportProfilesMatchProfilesResponse
                .respond201WithApplicationJson(entity.withProfile(profile).withId(profile.getId()), PostDataImportProfilesMatchProfilesResponse.headersFor201()))
              .otherwise(ExceptionHelper::mapExceptionToResponse)
              .onComplete(asyncResultHandler);
          }
        });
      } catch (Exception e) {
        logger.error("Failed to create Match Profile", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesMatchProfiles(boolean showDeleted, boolean withRelations, String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        matchProfileService.getProfiles(showDeleted, withRelations, query, offset, limit, tenantId)
          .map(GetDataImportProfilesMatchProfilesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Match Profiles");
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void putDataImportProfilesMatchProfilesById(String id, String lang, MatchProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                     Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_MARC_MARC_MATCH_PROFILE_ID, OCLC_INSTANCE_UUID_MATCH_PROFILE_ID)) {
          logger.error("Can`t update default OCLC Match Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t update default OCLC Job Profile with"))));
        } else {
          entity.getProfile().setMetadata(getMetadata(okapiHeaders));
          validateProfile(OperationType.UPDATE, entity.getProfile(), matchProfileService, tenantId).onComplete(errors -> {
            if (errors.failed()) {
              logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
              asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
            } else if (errors.result().getTotalRecords() > 0) {
              asyncResultHandler.handle(Future.succeededFuture(PutDataImportProfilesMatchProfilesByIdResponse.respond422WithApplicationJson(errors.result())));
            } else {
              entity.getProfile().setId(id);
              matchProfileService.updateProfile(entity, new OkapiConnectionParams(okapiHeaders))
                .map(updatedEntity -> (Response) PutDataImportProfilesMatchProfilesByIdResponse.respond200WithApplicationJson(updatedEntity))
                .otherwise(ExceptionHelper::mapExceptionToResponse)
                .onComplete(asyncResultHandler);
            }
          });
        }
      } catch (Exception e) {
        logger.error("Failed to update Match Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesMatchProfilesById(String id, boolean withRelations, String lang, Map<String, String> okapiHeaders,
                                                     Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        matchProfileService.getProfileById(id, withRelations, tenantId)
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(format("Match Profile with id '%s' was not found", id))))
          .map(GetDataImportProfilesMatchProfilesByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Match Profile by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void postDataImportProfilesMappingProfiles(String lang, MappingProfileUpdateDto entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.getProfile().setMetadata(getMetadata(okapiHeaders));
        validateMappingProfile(OperationType.CREATE, entity.getProfile(), tenantId).onComplete(errors -> {
          if (errors.failed()) {
            logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
            asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
          } else if (errors.result().getTotalRecords() > 0) {
            asyncResultHandler.handle(Future.succeededFuture(PostDataImportProfilesMappingProfilesResponse.respond422WithApplicationJson(errors.result())));
          } else {
            mappingProfileService.saveProfile(entity, new OkapiConnectionParams(okapiHeaders))
              .map(profile -> (Response) PostDataImportProfilesMappingProfilesResponse
                .respond201WithApplicationJson(entity.withProfile(profile).withId(profile.getId()), PostDataImportProfilesMappingProfilesResponse.headersFor201()))
              .otherwise(ExceptionHelper::mapExceptionToResponse)
              .onComplete(asyncResultHandler);
          }
        });
      } catch (Exception e) {
        logger.error("Failed to create Mapping Profile", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesMappingProfiles(boolean showDeleted, boolean withRelations, String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        mappingProfileService.getProfiles(showDeleted, withRelations, query, offset, limit, tenantId)
          .map(GetDataImportProfilesMappingProfilesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Mapping Profiles");
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void putDataImportProfilesMappingProfilesById(String id, String lang, MappingProfileUpdateDto entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_MAPPING_PROFILE_ID, OCLC_UPDATE_MAPPING_PROFILE_ID, OCLC_UPDATE_MARC_BIB_MAPPING_PROFILE_ID, DEFAULT_CREATE_DERIVE_HOLDINGS_MAPPING_PROFILE_ID, DEFAULT_CREATE_DERIVE_INSTANCE_MAPPING_PROFILE_ID)) {
          logger.error("Can`t update default OCLC Mapping Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t update default OCLC Mapping Profile"))));
        } else {
          entity.getProfile().setMetadata(getMetadata(okapiHeaders));
          validateMappingProfile(OperationType.UPDATE, entity.getProfile(), tenantId).onComplete(errors -> {
            if (errors.failed()) {
              logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
              asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
            } else if (errors.result().getTotalRecords() > 0) {
              asyncResultHandler.handle(Future.succeededFuture(PutDataImportProfilesMappingProfilesByIdResponse.respond422WithApplicationJson(errors.result())));
            } else {
              entity.getProfile().setId(id);
              mappingProfileService.updateProfile(entity, new OkapiConnectionParams(okapiHeaders))
                .map(updatedEntity -> (Response) PutDataImportProfilesMappingProfilesByIdResponse.respond200WithApplicationJson(updatedEntity))
                .otherwise(ExceptionHelper::mapExceptionToResponse)
                .onComplete(asyncResultHandler);
            }
          });
        }
      } catch (Exception e) {
        logger.error("Failed to update Mapping Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void deleteDataImportProfilesMappingProfilesById(String id, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_MAPPING_PROFILE_ID, OCLC_UPDATE_MAPPING_PROFILE_ID, OCLC_UPDATE_MARC_BIB_MAPPING_PROFILE_ID, DEFAULT_CREATE_DERIVE_HOLDINGS_MAPPING_PROFILE_ID, DEFAULT_CREATE_DERIVE_INSTANCE_MAPPING_PROFILE_ID)) {
          logger.error("Can`t delete default OCLC Mapping Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t delete default OCLC Mapping Profile"))));
        } else {
          OkapiConnectionParams params = new OkapiConnectionParams(okapiHeaders);
          mappingProfileService.markProfileAsDeleted(id, params.getTenantId())
            .map(DeleteDataImportProfilesMappingProfilesByIdResponse.respond204WithTextPlain(
              format("Mapping Profile with id '%s' was successfully deleted", id)))
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        }
      } catch (Exception e) {
        logger.error("Failed to delete Mapping Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesMappingProfilesById(String id, boolean withRelations, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        mappingProfileService.getProfileById(id, withRelations, tenantId)
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(format("Mapping Profile with id '%s' was not found", id))))
          .map(GetDataImportProfilesMappingProfilesByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Mapping Profile by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void deleteDataImportProfilesMatchProfilesById(String id, String lang, Map<String, String> okapiHeaders,
                                                        Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (id.equals(OCLC_MARC_MARC_MATCH_PROFILE_ID) || id.equals(OCLC_INSTANCE_UUID_MATCH_PROFILE_ID)) {
          logger.error("Can`t delete default OCLC Match Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t delete default OCLC Match Profile"))));
        } else {
          OkapiConnectionParams params = new OkapiConnectionParams(okapiHeaders);
          matchProfileService.markProfileAsDeleted(id, params.getTenantId())
            .map(DeleteDataImportProfilesMatchProfilesByIdResponse.respond204WithTextPlain(
              format("Match Profile with id '%s' was successfully deleted", id)))
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        }
      } catch (Exception e) {
        logger.error("Failed to delete Match Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void postDataImportProfilesActionProfiles(String lang, ActionProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                   Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.getProfile().setMetadata(getMetadata(okapiHeaders));
        validateProfile(OperationType.CREATE, entity.getProfile(), actionProfileService, tenantId).onComplete(errors -> {
          if (errors.failed()) {
            logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
            asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
          } else if (errors.result().getTotalRecords() > 0) {
            asyncResultHandler.handle(Future.succeededFuture(PostDataImportProfilesActionProfilesResponse.respond422WithApplicationJson(errors.result())));
          } else {
            actionProfileService.saveProfile(entity, new OkapiConnectionParams(okapiHeaders))
              .map(profile -> (Response) PostDataImportProfilesActionProfilesResponse
                .respond201WithApplicationJson(entity.withProfile(profile).withId(profile.getId()), PostDataImportProfilesActionProfilesResponse.headersFor201()))
              .otherwise(ExceptionHelper::mapExceptionToResponse)
              .onComplete(asyncResultHandler);
          }
        });
      } catch (Exception e) {
        logger.error("Failed to create Action Profile", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesActionProfiles(boolean showDeleted, boolean withRelations, String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        actionProfileService.getProfiles(showDeleted, withRelations, query, offset, limit, tenantId)
          .map(GetDataImportProfilesActionProfilesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Action Profiles");
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void putDataImportProfilesActionProfilesById(String id, String lang, ActionProfileUpdateDto entity, Map<String, String> okapiHeaders,
                                                      Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_INSTANCE_ACTION_PROFILE_ID, OCLC_UPDATE_INSTANCE_ACTION_PROFILE_ID, OCLC_UPDATE_MARC_BIB_ACTION_PROFILE_ID, DEFAULT_CREATE_DERIVE_HOLDINGS_ACTION_PROFILE_ID, DEFAULT_CREATE_DERIVE_INSTANCE_ACTION_PROFILE_ID)) {
          logger.error("Can`t update default OCLC Action Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t update default OCLC Action Profile"))));
        } else {
          entity.getProfile().setMetadata(getMetadata(okapiHeaders));
          validateProfile(OperationType.UPDATE, entity.getProfile(), actionProfileService, tenantId).onComplete(errors -> {
            if (errors.failed()) {
              logger.error(format(PROFILE_VALIDATE_ERROR_MESSAGE, entity.getClass().getSimpleName()), errors.cause());
              asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(errors.cause())));
            } else if (errors.result().getTotalRecords() > 0) {
              asyncResultHandler.handle(Future.succeededFuture(PutDataImportProfilesActionProfilesByIdResponse.respond422WithApplicationJson(errors.result())));
            } else {
              entity.getProfile().setId(id);
              actionProfileService.updateProfile(entity, new OkapiConnectionParams(okapiHeaders))
                .map(updatedEntity -> (Response) PutDataImportProfilesActionProfilesByIdResponse.respond200WithApplicationJson(updatedEntity))
                .otherwise(ExceptionHelper::mapExceptionToResponse)
                .onComplete(asyncResultHandler);
            }
          });
        }
      } catch (Exception e) {
        logger.error("Failed to update Action Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesActionProfilesById(String id, boolean withRelations, String lang, Map<String, String> okapiHeaders,
                                                      Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        actionProfileService.getProfileById(id, withRelations, tenantId)
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(format("Action Profile with id '%s' was not found", id))))
          .map(GetDataImportProfilesActionProfilesByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Action Profile by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void postDataImportProfilesProfileAssociations(String master, String detail, String lang, ProfileAssociation entity, Map<String, String> okapiHeaders,
                                                        Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        profileAssociationService.save(entity, mapContentType(master), mapContentType(detail), tenantId)
          .map((Response) PostDataImportProfilesProfileAssociationsResponse
            .respond201WithApplicationJson(entity, PostDataImportProfilesProfileAssociationsResponse.headersFor201()))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to create Profile association", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesProfileAssociations(String master, String detail, String lang, Map<String, String> okapiHeaders,
                                                       Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
        try {
          OkapiConnectionParams params = new OkapiConnectionParams(okapiHeaders);
          profileAssociationService.getAll(mapContentType(master), mapContentType(detail), params.getTenantId())
            .map(GetDataImportProfilesProfileAssociationsResponse::respond200WithApplicationJson)
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        } catch (Exception e) {
          logger.error("Failed to get ProfileAssociations by masterType '{}' and detailType '{}", master, detail, e);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
        }
      }
    );
  }

  @Override
  public void putDataImportProfilesProfileAssociationsById(String id, String master, String detail, String lang, ProfileAssociation entity,
                                                           Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        entity.setId(id);
        profileAssociationService.update(entity, mapContentType(master), mapContentType(detail), new OkapiConnectionParams(okapiHeaders))
          .map(updatedEntity -> (Response) PutDataImportProfilesProfileAssociationsByIdResponse.respond200WithApplicationJson(updatedEntity))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to update Profile association with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void deleteDataImportProfilesProfileAssociationsById(String id, String master, String detail, String lang, Map<String, String> okapiHeaders,
                                                              Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        profileAssociationService.delete(id, mapContentType(master), mapContentType(detail), tenantId)
          .map(deleted -> deleted
            ? DeleteDataImportProfilesProfileAssociationsByIdResponse.respond204WithTextPlain(
            format("Profile association with id '%s' was successfully deleted", id))
            : DeleteDataImportProfilesProfileAssociationsByIdResponse.respond404WithTextPlain(
            format("Profile association with id '%s' was not found", id)))
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to delete Profile association with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesProfileAssociationsById(
    String id,
    String master,
    String detail,
    String lang,
    Map<String, String> okapiHeaders,
    Handler<AsyncResult<Response>> asyncResultHandler,
    Context vertxContext) {

    vertxContext.runOnContext(c -> {
      try {
        OkapiConnectionParams params = new OkapiConnectionParams(okapiHeaders);
        profileAssociationService.getById(id, mapContentType(master), mapContentType(detail), params.getTenantId())
          .map(optionalProfile -> optionalProfile.orElseThrow(() ->
            new NotFoundException(format("Profile association with id '%s' was not found", id))))
          .map(GetDataImportProfilesProfileAssociationsByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Profile association by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesJobProfileSnapshotsById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(c -> {
      try {
        profileSnapshotService.getById(id, tenantId)
          .map(optionalSnapshot -> optionalSnapshot.orElseThrow(() ->
            new NotFoundException(format("Profile snapshot with id '%s' was not found", id))))
          .map(GetDataImportProfilesJobProfileSnapshotsByIdResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get Profile snapshot by id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void postDataImportProfilesJobProfileSnapshotsById(String jobProfileId, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        profileSnapshotService.createSnapshot(jobProfileId, tenantId)
          .map(snapshot -> (Response) PostDataImportProfilesProfileAssociationsResponse
            .respond201WithApplicationJson(snapshot, PostDataImportProfilesProfileAssociationsResponse.headersFor201()))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to create Profile association", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesProfileAssociationsDetailsById(
    String id,
    String masterType,
    String detailType,
    String query,
    int offset,
    int limit,
    Map<String, String> okapiHeaders,
    Handler<AsyncResult<Response>> asyncResultHandler,
    Context vertxContext) {

    vertxContext.runOnContext(event -> {
        try {
          profileAssociationService.findDetails(id, mapContentType(masterType), mapContentTypeOrNull(detailType), query, offset, limit, tenantId)
            .map(optional -> optional.orElseThrow(() -> new NotFoundException(format(MASTER_PROFILE_NOT_FOUND_MSG, id))))
            .map(GetDataImportProfilesProfileAssociationsDetailsByIdResponse::respond200WithApplicationJson)
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        } catch (Exception e) {
          logger.error("Failed to retrieve details by master profile with id {}", id, e);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
        }
      }
    );
  }

  private ContentType mapContentTypeOrNull(String detailType) {
    return Arrays.stream(ContentType.values())
      .filter(it -> it.value().equals(detailType))
      .findFirst()
      .orElse(null);
  }

  @Override
  public void getDataImportProfilesProfileAssociationsMastersById(
    String id,
    String detailType,
    String masterType,
    String query,
    int offset,
    int limit,
    Map<String, String> okapiHeaders,
    Handler<AsyncResult<Response>> asyncResultHandler,
    Context vertxContext) {

    vertxContext.runOnContext(event -> {
        try {
          profileAssociationService.findMasters(id, mapContentType(detailType), mapContentTypeOrNull(masterType), query, offset, limit, tenantId)
            .map(optional -> optional.orElseThrow(() -> new NotFoundException(format(DETAIL_PROFILE_NOT_FOUND_MSG, id))))
            .map(GetDataImportProfilesProfileAssociationsMastersByIdResponse::respond200WithApplicationJson)
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        } catch (Exception e) {
          logger.error("Failed to retrieve masters by detail profile with id {}", id, e);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
        }
      }
    );
  }

  @Override
  public void deleteDataImportProfilesActionProfilesById(String id, String lang, Map<String, String> okapiHeaders,
                                                         Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        if (canDeleteOrUpdateProfile(id, OCLC_CREATE_INSTANCE_ACTION_PROFILE_ID, OCLC_UPDATE_INSTANCE_ACTION_PROFILE_ID, OCLC_UPDATE_MARC_BIB_ACTION_PROFILE_ID, DEFAULT_CREATE_DERIVE_HOLDINGS_ACTION_PROFILE_ID, DEFAULT_CREATE_DERIVE_INSTANCE_ACTION_PROFILE_ID)) {
          logger.error("Can`t delete default OCLC Action Profile with id {}", id);
          asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(new BadRequestException("Can`t delete default OCLC Action Profile"))));
        } else {
          actionProfileService.markProfileAsDeleted(id, tenantId)
            .map(DeleteDataImportProfilesActionProfilesByIdResponse.respond204WithTextPlain(
              format("Action Profile with id '%s' was successfully deleted", id)))
            .map(Response.class::cast)
            .otherwise(ExceptionHelper::mapExceptionToResponse)
            .onComplete(asyncResultHandler);
        }
      } catch (Exception e) {
        logger.error("Failed to delete Action Profile with id {}", id, e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesEntityTypes(Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        actionProfileService.getEntityTypes()
          .map(GetDataImportProfilesEntityTypesResponse::respond200WithApplicationJson)
          .map(Response.class::cast)
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to get all entity types", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  @Override
  public void getDataImportProfilesProfileSnapshotsByProfileId(String id, String profileType, String jobProfileId, Map<String, String> okapiHeaders,
                                                               Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    vertxContext.runOnContext(v -> {
      try {
        profileSnapshotService.constructSnapshot(id, mapContentType(profileType), jobProfileId, tenantId)
          .map(snapshot -> (Response) GetDataImportProfilesProfileSnapshotsByProfileIdResponse
            .respond200WithApplicationJson(snapshot))
          .otherwise(ExceptionHelper::mapExceptionToResponse)
          .onComplete(asyncResultHandler);
      } catch (Exception e) {
        logger.error("Failed to construct Profile Snapshot", e);
        asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
      }
    });
  }

  private <T, S, D> Future<Errors> validateProfile(OperationType operationType, T profile, ProfileService<T, S, D> profileService, String tenantId) {
    Promise<Errors> promise = Promise.promise();
    String profileTypeName = StringUtils.uncapitalize(profile.getClass().getSimpleName());
    Map<String, Future<Boolean>> validateConditions = getValidateConditions(operationType, profile, profileService, tenantId);
    List<String> errorCodes = new ArrayList<>(validateConditions.keySet());
    List<Future<Boolean>> futures = new ArrayList<>(validateConditions.values());
    GenericCompositeFuture.all(futures).onComplete( ar -> {
      if(ar.succeeded()) {
        List<Error> errors = new ArrayList<>(errorCodes).stream()
          .filter(errorCode -> ar.result().resultAt(errorCodes.indexOf(errorCode)))
          .map(errorCode -> new Error().withMessage(format(errorCode, profileTypeName)))
          .collect(Collectors.toList());
        promise.complete(new Errors().withErrors(errors).withTotalRecords(errors.size()));
      } else {
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }

  private <T, S, D> Map<String, Future<Boolean>> getValidateConditions(OperationType operationType, T profile, ProfileService<T, S, D> profileService, String tenantId) {
    Map<String, Future<Boolean>> validateConditions = new LinkedHashMap<>();
    validateConditions.put(DUPLICATE_PROFILE_ERROR_CODE, profileService.isProfileExistByProfileName(profile, tenantId));
    if(operationType == OperationType.CREATE) {
      validateConditions.put(DUPLICATE_PROFILE_ID_ERROR_CODE, profileService.isProfileExistByProfileId(profile, tenantId));
    }
    return validateConditions;
  }

  private Future<Errors> validateMappingProfile(OperationType operationType, MappingProfile mappingProfile, String tenantId) {
    return validateProfile(operationType, mappingProfile, mappingProfileService, tenantId)
      .map(errors -> {
        List<Error> fieldsValidationErrors = validateRepeatableFields(mappingProfile);
        errors.withTotalRecords(errors.getTotalRecords() + fieldsValidationErrors.size())
          .getErrors().addAll(fieldsValidationErrors);
        return errors;
      });
  }

  private List<Error> validateRepeatableFields(MappingProfile mappingProfile) {
    List<Error> errorList = new ArrayList<>();
    if (mappingProfile.getMappingDetails() != null && mappingProfile.getMappingDetails().getMappingFields() != null) {
      List<MappingRule> mappingFields = mappingProfile.getMappingDetails().getMappingFields();
      for (MappingRule rule : mappingFields) {
        if (rule.getRepeatableFieldAction() != null && rule.getSubfields().isEmpty() && !rule.getRepeatableFieldAction().equals(MappingRule.RepeatableFieldAction.DELETE_EXISTING)) {
          errorList.add(new Error().withMessage(format(INVALID_REPEATABLE_FIELD_ACTION_FOR_EMPTY_SUBFIELDS_MESSAGE, rule.getRepeatableFieldAction())));
        }
      }
    }
    return errorList;
  }

  private ContentType mapContentType(String contentType) {
    try {
      return ContentType.fromValue(contentType);
    } catch (IllegalArgumentException e) {
      String message = "The specified type: %s is wrong. It should be " + Arrays.toString(ContentType.values());
      throw new BadRequestException(format(message, contentType), e);
    }
  }

  private Metadata getMetadata(Map<String, String> okapiHeaders) {
    String userId = okapiHeaders.get(OKAPI_USERID_HEADER);
    String token = okapiHeaders.get(OKAPI_HEADER_TOKEN);
    if (userId == null && token != null) {
      userId = userIdFromToken(token);
    }
    Metadata md = new Metadata();
    md.setUpdatedDate(new Date());
    md.setUpdatedByUserId(userId);
    md.setCreatedDate(md.getUpdatedDate());
    md.setCreatedByUserId(userId);
    return md;
  }

  private static String userIdFromToken(String token) {
    try {
      String[] split = token.split("\\.");
      String json = getJson(split[1]);
      JsonObject j = new JsonObject(json);
      return j.getString("user_id");
    } catch (Exception e) {
      logger.warn("Invalid x-okapi-token: " + token, e);
      return null;
    }
  }

  private static String getJson(String strEncoded) {
    byte[] decodedBytes = Base64.getDecoder().decode(strEncoded);
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }

  private boolean canDeleteOrUpdateProfile(String id, String... uids) {
    return Arrays.asList(uids).contains(id);
  }

}
