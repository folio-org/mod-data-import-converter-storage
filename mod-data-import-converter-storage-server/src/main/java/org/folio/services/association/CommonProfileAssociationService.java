package org.folio.services.association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Generic implementation of the {@link ProfileAssociationService}
 */
@Service
public class CommonProfileAssociationService implements ProfileAssociationService { //NOSOANR

  @Autowired
  private ProfileAssociationDao<JobProfileCollection, ActionProfileCollection> jobToActionProfile;
  @Autowired
  private ProfileDao<JobProfile, JobProfileCollection> jobProfileDao;
  @Autowired
  private ProfileDao<ActionProfile, ActionProfileCollection> actionProfileDao;
  @Autowired
  private ProfileDao<MappingProfile, MappingProfileCollection> mappingProfileDao;
  @Autowired
  private ProfileDao<MatchProfile, MatchProfileCollection> matchProfileDao;

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, String tenantId) {
    return jobToActionProfile.getById(id, tenantId);
  }

  @Override
  public Future<ProfileAssociation> save(ProfileAssociation entity, OkapiConnectionParams params) {
    entity.setId(UUID.randomUUID().toString());
    return jobToActionProfile.save(entity, params.getTenantId()).map(entity);
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, OkapiConnectionParams params) {
    return jobToActionProfile.update(entity, params.getTenantId());
  }

  @Override
  public Future<Boolean> delete(String id, String tenantId) {
    return jobToActionProfile.delete(id, tenantId);
  }


  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findDetails(String tenantId, String masterId, ContentType masterType) {

    Future<Optional<ProfileSnapshotWrapper>> result = Future.future();

    jobToActionProfile.getDetailProfilesByMasterId(tenantId, masterId)
      .setHandler(ar -> {
        if (ar.failed()) {
          result.fail(ar.cause());
        }
        List<ChildSnapshotWrapper> details = ar.result();
        ProfileSnapshotWrapper wrapper = getProfileWrapper(masterId, masterType, details);
        fillProfile(tenantId, result, wrapper);
      });

    return result;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> findMasters(String tenantId, String detailId, ContentType detailType) {

    Future<Optional<ProfileSnapshotWrapper>> result = Future.future();

    jobToActionProfile.getMasterProfilesByDetailId(tenantId, detailId)
      .setHandler(ar -> {
        if (ar.failed()) {
          result.fail(ar.cause());
        }
        ProfileSnapshotWrapper wrapper = getProfileWrapper(detailId, detailType, ar.result());
        fillProfile(tenantId, result, wrapper);
      });

    return result;
  }

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

  private ProfileSnapshotWrapper getProfileWrapper(String profileId, ContentType profileType, List<ChildSnapshotWrapper> children) {
    ProfileSnapshotWrapper wrapper = new ProfileSnapshotWrapper();
    wrapper.setChildSnapshotWrappers(children);
    wrapper.setId(profileId);
    wrapper.setContentType(ContentType.fromValue(profileType.value()));
    return wrapper;
  }

  private <T> Handler<AsyncResult<Optional<T>>> fillWrapperContent(Future<Optional<ProfileSnapshotWrapper>> result, ProfileSnapshotWrapper wrapper) {
    return asyncResult -> {
      if (asyncResult.failed()) result.fail(asyncResult.cause());

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
