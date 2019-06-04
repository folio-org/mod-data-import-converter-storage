package org.folio.services.association;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.ProfileDao;
import org.folio.dao.association.ProfileAssociationDao;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileAssociationCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;


/**
 * Generic implementation of the {@link ProfileAssociationService}
 */
@Service
public class CommonProfileAssociationService implements ProfileAssociationService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonProfileAssociationService.class);
  private static final String PROFILE_ASSOCIATION_TYPE_PATTERN = "%s_TO_%s";
  private static final String CORRECT_PROFILE_ASSOCIATION_TYPES_MESSAGE = "Correct ProfileAssociation types: ACTION_PROFILE_TO_ACTION_PROFILE, ACTION_PROFILE_TO_MAPPING_PROFILE, "
    + "ACTION_PROFILE_TO_MATCH_PROFILE, JOB_PROFILE_TO_ACTION_PROFILE, JOB_PROFILE_TO_MATCH_PROFILE, MATCH_PROFILE_TO_ACTION_PROFILE, MATCH_PROFILE_TO_MATCH_PROFILE";

  @Autowired
  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;
  @Autowired
  private ProfileDao<ActionProfile, ActionProfileCollection> actionProfileDao;
  @Autowired
  private ProfileDao<MappingProfile, MappingProfileCollection> mappingProfileDao;
  @Autowired
  private ProfileDao<MatchProfile, MatchProfileCollection> matchProfileDao;
  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public Future<ProfileAssociationCollection> getAll(ContentType masterType, ContentType detailType, String tenantId) {
    return getProfileAssociationDao(masterType, detailType).getAll(tenantId);
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, ContentType masterType, ContentType detailType, String tenantId) {
    return getProfileAssociationDao(masterType, detailType).getById(id, tenantId);
  }

  @Override
  public Future<ProfileAssociation> save(ProfileAssociation entity, ContentType masterType, ContentType detailType, OkapiConnectionParams params) {
    entity.setId(UUID.randomUUID().toString());
    return getProfileAssociationDao(masterType, detailType).save(entity, params.getTenantId()).map(entity);
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, ContentType masterType, ContentType detailType, OkapiConnectionParams params) {
    return getProfileAssociationDao(masterType, detailType).update(entity, params.getTenantId());
  }

  @Override
  public Future<Boolean> delete(String id, ContentType masterType, ContentType detailType, String tenantId) {
    return getProfileAssociationDao(masterType, detailType).delete(id, tenantId);
  }


  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findDetails(String masterId, ContentType masterType, ContentType detailType, String query, int offset, int limit, String tenantId) {

    Future<Optional<ProfileSnapshotWrapper>> result = Future.future();

    getProfileAssociationDao(masterType, detailType).getDetailProfilesByMasterId(masterId, detailType, query, offset, limit, tenantId)
      .setHandler(ar -> {
        if (ar.failed()) {
          LOGGER.error("Could not get details profiles by master id '{}', for the tenant '{}'", masterId, tenantId);
          result.fail(ar.cause());
        }
        List<ChildSnapshotWrapper> details = ar.result();
        ProfileSnapshotWrapper wrapper = getProfileWrapper(masterId, masterType, details);
        fillProfile(tenantId, result, wrapper);
      });

    return result;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findMasters(String detailId, ContentType detailType, ContentType masterType, String query, int offset, int limit, String tenantId) {

    Future<Optional<ProfileSnapshotWrapper>> result = Future.future();

    getProfileAssociationDao(masterType, detailType).getMasterProfilesByDetailId(detailId, masterType, query, offset, limit, tenantId)
      .setHandler(ar -> {
        if (ar.failed()) {
          LOGGER.error("Could not get master profiles by detail id '{}', for the tenant '{}'", detailId, tenantId);
          result.fail(ar.cause());
        }
        ProfileSnapshotWrapper wrapper = getProfileWrapper(detailId, detailType, ar.result());
        fillProfile(tenantId, result, wrapper);
      });

    return result;
  }

  /**
   * Returns ProfileAssociationDao instance according to profile association type,
   * which is built on the pattern {masterType}_TO_{detailType}.
   *
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @return ProfileAssociationDao implementation
   */
  private ProfileAssociationDao getProfileAssociationDao(ContentType masterType, ContentType detailType) {
    String profileAssociationType = format(PROFILE_ASSOCIATION_TYPE_PATTERN, masterType.value(), detailType.value());
    try {
      return (ProfileAssociationDao) applicationContext.getBean(profileAssociationType);
    } catch (BeansException e) {
      String message = format("Invalid ProfileAssociation type with master type '%s' and detail type '%s'. ", masterType, detailType);
      LOGGER.error(message);
      throw new BadRequestException(message + CORRECT_PROFILE_ASSOCIATION_TYPES_MESSAGE);
    }
  }

  /**
   * Retrieves profile by profile id and profile type and then fill profile wrapper with the instance.
   *
   * @param tenantId a tenant id.
   * @param result   a result future.
   * @param wrapper  a profile wrapper.
   */
  private void fillProfile(String tenantId, Future<Optional<ProfileSnapshotWrapper>> result, ProfileSnapshotWrapper wrapper) {
    String profileId = wrapper.getId();
    ContentType profileType = wrapper.getContentType();

    if (profileType == ContentType.JOB_PROFILE) {
      jobProfileDao.getProfileById(profileId, tenantId).setHandler(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.ACTION_PROFILE) {
      actionProfileDao.getProfileById(profileId, tenantId).setHandler(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.MAPPING_PROFILE) {
      mappingProfileDao.getProfileById(profileId, tenantId).setHandler(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.MATCH_PROFILE) {
      matchProfileDao.getProfileById(profileId, tenantId).setHandler(fillWrapperContent(result, wrapper));
    } else {
      result.complete(Optional.empty());
    }
  }

  /**
   * Creates a profile wrapper.
   *
   * @param profileId   a profile id.
   * @param profileType a profile type.
   * @param children    a list of children
   * @return profile wrapper
   */
  private ProfileSnapshotWrapper getProfileWrapper(String profileId, ContentType profileType, List<ChildSnapshotWrapper> children) {
    ProfileSnapshotWrapper wrapper = new ProfileSnapshotWrapper();
    wrapper.setChildSnapshotWrappers(children);
    wrapper.setId(profileId);
    wrapper.setContentType(ContentType.fromValue(profileType.value()));
    return wrapper;
  }

  /**
   * Fills a profile wrapper with a profile instance if it's present otherwise it will complete result future with empty optional.
   *
   * @param result  a future result.
   * @param wrapper a profile wrapper.
   * @param <T>     a profile type.
   * @return the handler.
   */
  private <T> Handler<AsyncResult<Optional<T>>> fillWrapperContent(Future<Optional<ProfileSnapshotWrapper>> result, ProfileSnapshotWrapper wrapper) {
    return asyncResult -> {
      if (asyncResult.failed()) {
        LOGGER.error("Could not get a profile", asyncResult.cause());
        result.fail(asyncResult.cause());
      }

      Optional<T> resultOptional = asyncResult.result();
      if (resultOptional.isPresent()) {
        wrapper.setContent(resultOptional.get());
        result.complete(Optional.of(wrapper));
      } else {
        result.complete(Optional.empty());
      }
    };
  }


}
