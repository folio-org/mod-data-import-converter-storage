package org.folio.services.snapshot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.dao.snapshot.ProfileSnapshotItem;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;

/**
 * Implementation for Profile snapshot service
 */
@Service
public class ProfileSnapshotServiceImpl implements ProfileSnapshotService {

  private static final Logger LOGGER = LogManager.getLogger();
  private final ProfileSnapshotDao profileSnapshotDao;
  private final Cache<String, ProfileSnapshotWrapper> profileSnapshotWrapperCache;
  private final Executor cacheExecutor = runnable -> {
    Context context = Vertx.currentContext();
    if (context != null) {
      context.runOnContext(ar -> runnable.run());
    } else {
      // The common pool below is used because it is the  default executor for caffeine
      ForkJoinPool.commonPool().execute(runnable);
    }
  };

  public ProfileSnapshotServiceImpl(@Autowired ProfileSnapshotDao profileSnapshotDao) {
    this.profileSnapshotDao = profileSnapshotDao;
    this.profileSnapshotWrapperCache = Caffeine.newBuilder()
      .maximumSize(20)
      .executor(cacheExecutor)
      .build();
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId) {
    final String cacheKey = tenantId + id;
    ProfileSnapshotWrapper profileSnapshotWrapper = profileSnapshotWrapperCache.getIfPresent(cacheKey);
    if (profileSnapshotWrapper == null) {
      return profileSnapshotDao.getById(id, tenantId)
        .map(optionalWrapper ->
          optionalWrapper.map(this::convertProfileSnapshotWrapperContent))
        .onSuccess(wrapper -> {
          if (wrapper.isEmpty()) {
            return;
          }
          profileSnapshotWrapperCache.put(cacheKey, wrapper.get());
        });
    }
    return Future.succeededFuture(Optional.of(profileSnapshotWrapper));
  }

  @Override
  public Future<ProfileSnapshotWrapper> createSnapshot(String jobProfileId, String tenantId) {
    Promise<ProfileSnapshotWrapper> promise = Promise.promise();
    return constructSnapshot(jobProfileId, JOB_PROFILE, jobProfileId, tenantId)
      .compose(rootWrapper -> {
        profileSnapshotDao.save(rootWrapper, tenantId).onComplete(savedAr -> {
          if (savedAr.failed()) {
            promise.fail(savedAr.cause());
          } else {
            promise.complete(rootWrapper);
          }
        });
        return promise.future();
      });
  }

  @Override
  public Future<ProfileSnapshotWrapper> constructSnapshot(String profileId, ProfileSnapshotWrapper.ContentType profileType, String jobProfileId, String tenantId) {
    Promise<ProfileSnapshotWrapper> promise = Promise.promise();
    return profileSnapshotDao.getSnapshotItems(profileId, profileType, jobProfileId, tenantId)
      .compose(snapshotItems -> {
        if (CollectionUtils.isEmpty(snapshotItems)) {
          String errorMessage = "constructSnapshot:: Cannot build snapshot for Profile " + profileId;
          LOGGER.warn(errorMessage);
          promise.fail(errorMessage);
        } else {
          promise.complete(buildSnapshot(snapshotItems));
        }
        return promise.future();
      });
  }

  /**
   * Creates ProfileSnapshotWrapper traversing through collection of profile snapshot items.
   *
   * @param snapshotItems list of snapshot items (rows)
   * @return root snapshot (ProfileSnapshotWrapper) with child items (ChildSnapshotWrapper) inside
   */
  private ProfileSnapshotWrapper buildSnapshot(List<ProfileSnapshotItem> snapshotItems) {
    /* We need to remove duplicates to avoid double-appearance of the same child profiles in diamond inheritance */
    removeDuplicatesByAssociationId(snapshotItems);

    Optional<ProfileSnapshotItem> optionalRootItem = snapshotItems.stream().filter(item -> item.getMasterId() == null).findFirst();
    if (optionalRootItem.isPresent()) {
      ProfileSnapshotItem rootItem = optionalRootItem.get();
      ProfileSnapshotWrapper rootWrapper = new ProfileSnapshotWrapper();
      if (rootItem.getReactTo() != null) {
        rootWrapper.setReactTo(ProfileSnapshotWrapper.ReactTo.fromValue(rootItem.getReactTo().name()));
      }
      rootWrapper.setOrder(rootItem.getOrder());
      rootWrapper.setId(UUID.randomUUID().toString());
      rootWrapper.setProfileId(rootItem.getDetailId());
      rootWrapper.setContentType(rootItem.getDetailType());
      rootWrapper.setContent(convertContentByType(rootItem.getDetail(), rootItem.getDetailType()));
      fillChildSnapshotWrappers(rootItem.getDetailId(), rootWrapper.getChildSnapshotWrappers(), snapshotItems);
      return rootWrapper;
    } else {
      throw new IllegalArgumentException("Can not find the root item in snapshot items list");
    }
  }

  /**
   * Fills given collection by child wrappers traversing through snapshot.
   * The method finds a first child of given parent profile, adds a child to parent profile(in childWrappers collection)
   * and falls into recursion passing child profile just been found (Depth-first traversal algorithm).
   *
   * @param parentId      parent profile id
   * @param childWrappers collection of child snapshot wrappers linked to given parent id
   * @param snapshotItems collection of snapshot items
   */
  private void fillChildSnapshotWrappers(String parentId, List<ProfileSnapshotWrapper> childWrappers, List<ProfileSnapshotItem> snapshotItems) {
    for (ProfileSnapshotItem snapshotItem : snapshotItems) {
      if (parentId.equals(snapshotItem.getMasterId())) {
        ProfileSnapshotWrapper childWrapper = new ProfileSnapshotWrapper();
        childWrapper.setId(UUID.randomUUID().toString());
        childWrapper.setProfileId(snapshotItem.getDetailId());
        childWrapper.setContentType(snapshotItem.getDetailType());
        childWrapper.setContent(convertContentByType(snapshotItem.getDetail(), snapshotItem.getDetailType()));
        if (snapshotItem.getReactTo() != null) {
          childWrapper.setReactTo(ProfileSnapshotWrapper.ReactTo.fromValue(snapshotItem.getReactTo().name()));
        }
        childWrapper.setOrder(snapshotItem.getOrder());
        childWrappers.add(childWrapper);
        fillChildSnapshotWrappers(childWrapper.getProfileId(), childWrapper.getChildSnapshotWrappers(), snapshotItems);
      }
    }
  }

  /**
   * Removes the items with the same association id
   *
   * @param snapshotItems collection of snapshot items (rows)
   */
  private void removeDuplicatesByAssociationId(List<ProfileSnapshotItem> snapshotItems) {
    Set<String> duplicates = new HashSet<>(snapshotItems.size());
    snapshotItems.removeIf(current -> !duplicates.add(current.getAssociationId()));
  }

  /**
   * Method converts an Object 'content' field to concrete Profile class doing the same for all the child wrappers.
   * to concrete Profile class. The class resolution happens by 'content type' field.
   *
   * @param wrapper the given ProfileSnapshotWrapper
   * @return ProfileSnapshotWrapper with converted 'content' field
   */
  private ProfileSnapshotWrapper convertProfileSnapshotWrapperContent(@NotNull ProfileSnapshotWrapper wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (ProfileSnapshotWrapper child : wrapper.getChildSnapshotWrappers()) {
      convertProfileSnapshotWrapperContent(child);
    }
    return wrapper;
  }

  /**
   * Method converts an Object 'content' field to concrete Profile class.
   *
   * @param content     wrapper's content
   * @param contentType type of wrapper's content
   * @param <T>         concrete class of the Profile
   * @return concrete class of the Profile
   */
  private <T> T convertContentByType(Object content, ProfileSnapshotWrapper.ContentType contentType) {
    ObjectMapper mapper = new ObjectMapper();
    switch (contentType) {
      case JOB_PROFILE:
        return (T) mapper.convertValue(content, JobProfile.class);
      case MATCH_PROFILE:
        return (T) mapper.convertValue(content, MatchProfile.class);
      case ACTION_PROFILE:
        return (T) mapper.convertValue(content, ActionProfile.class);
      case MAPPING_PROFILE:
        return (T) mapper.convertValue(content, MappingProfile.class);
      default:
        throw new IllegalStateException("Can not find profile by snapshot content type: " + contentType.toString());
    }
  }
}
