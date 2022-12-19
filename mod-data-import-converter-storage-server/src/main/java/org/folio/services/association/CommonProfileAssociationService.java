package org.folio.services.association;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.dao.ProfileDao;
import org.folio.dao.association.MasterDetailAssociationDao;
import org.folio.dao.association.ProfileAssociationDao;
import org.folio.rest.impl.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Generic implementation of the {@link ProfileAssociationService}
 */
@Service
public class CommonProfileAssociationService implements ProfileAssociationService {
  private static final Logger LOGGER = LogManager.getLogger();

  @Autowired
  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;
  @Autowired
  private ProfileDao<ActionProfile, ActionProfileCollection> actionProfileDao;
  @Autowired
  private ProfileDao<MappingProfile, MappingProfileCollection> mappingProfileDao;
  @Autowired
  private ProfileDao<MatchProfile, MatchProfileCollection> matchProfileDao;
  @Autowired
  private ProfileAssociationDao profileAssociationDao;
  @Autowired
  private MasterDetailAssociationDao masterDetailAssociationDao;

  @Override
  public Future<ProfileAssociationCollection> getAll(ContentType masterType, ContentType detailType, String tenantId) {
    return profileAssociationDao.getAll(masterType, detailType, tenantId);
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, ContentType masterType, ContentType detailType, String tenantId) {
    return profileAssociationDao.getById(id, masterType, detailType, tenantId);
  }

  @Override
  public Future<ProfileAssociation> save(ProfileAssociation entity, ContentType masterType, ContentType detailType, String tenantId) {
    entity.setId(UUID.randomUUID().toString());
    return profileAssociationDao.save(entity, masterType, detailType, tenantId).map(entity);
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, ContentType masterType, ContentType detailType, OkapiConnectionParams params) {
    return profileAssociationDao.update(entity, masterType, detailType, params.getTenantId());
  }

  @Override
  public Future<Boolean> delete(String id, ContentType masterType, ContentType detailType, String tenantId) {
    return profileAssociationDao.delete(id, masterType, detailType, tenantId);
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findDetails(String masterId, ContentType masterType, ContentType detailType, String query, int offset, int limit, String tenantId) {
    Promise<Optional<ProfileSnapshotWrapper>> result = Promise.promise();

    masterDetailAssociationDao.getDetailProfilesByMasterId(masterId, detailType, query, offset, limit, tenantId)
      .onComplete(ar -> {
        if (ar.failed()) {
          LOGGER.warn("findDetails:: Could not get details profiles by master id '{}', for the tenant '{}'", masterId, tenantId);
          result.fail(ar.cause());
        }
        List<ProfileSnapshotWrapper> details = ar.result();
        ProfileSnapshotWrapper wrapper = getProfileWrapper(masterId, masterType, details);
        fillProfile(tenantId, result, wrapper);
      });

    return result.future();
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findMasters(String detailId, ContentType detailType, ContentType masterType, String query, int offset, int limit, String tenantId) {
    Promise<Optional<ProfileSnapshotWrapper>> result = Promise.promise();

    masterDetailAssociationDao.getMasterProfilesByDetailId(detailId, masterType, query, offset, limit, tenantId)
      .onComplete(ar -> {
        if (ar.failed()) {
          LOGGER.warn("findMasters:: Could not get master profiles by detail id '{}', for the tenant '{}'", detailId, tenantId);
          result.fail(ar.cause());
        }
        ProfileSnapshotWrapper wrapper = getProfileWrapper(detailId, detailType, ar.result());
        fillProfile(tenantId, result, wrapper);
      });

    return result.future();
  }

  @Override
  public Future<Boolean> delete(String masterId, String detailId, ContentType masterType, ContentType detailType, String tenantId, String jobProfileId) {
    return profileAssociationDao.delete(masterId, detailId, masterType, detailType, tenantId, jobProfileId);
  }

  @Override
  public Future<Boolean> deleteByMasterId(String masterId, ContentType masterType, ContentType detailType, String tenantId) {
    return profileAssociationDao.deleteByMasterId(masterId, masterType, detailType, tenantId);
  }

  /**
   * Retrieves profile by profile id and profile type and then fill profile wrapper with the instance.
   *
   * @param tenantId a tenant id.
   * @param result   a result future.
   * @param wrapper  a profile wrapper.
   */
  private void fillProfile(String tenantId, Promise<Optional<ProfileSnapshotWrapper>> result, ProfileSnapshotWrapper wrapper) {
    String profileId = wrapper.getId();
    ContentType profileType = wrapper.getContentType();

    if (profileType == ContentType.JOB_PROFILE) {
      jobProfileDao.getProfileById(profileId, tenantId).onComplete(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.ACTION_PROFILE) {
      actionProfileDao.getProfileById(profileId, tenantId).onComplete(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.MAPPING_PROFILE) {
      mappingProfileDao.getProfileById(profileId, tenantId).onComplete(fillWrapperContent(result, wrapper));
    } else if (profileType == ContentType.MATCH_PROFILE) {
      matchProfileDao.getProfileById(profileId, tenantId).onComplete(fillWrapperContent(result, wrapper));
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
  private ProfileSnapshotWrapper getProfileWrapper(String profileId, ContentType profileType, List<ProfileSnapshotWrapper> children) {
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
  private <T> Handler<AsyncResult<Optional<T>>> fillWrapperContent(Promise<Optional<ProfileSnapshotWrapper>> result, ProfileSnapshotWrapper wrapper) {
    return asyncResult -> {
      if (asyncResult.failed()) {
        LOGGER.warn("fillWrapperContent:: Could not get a profile", asyncResult.cause());
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
