package org.folio.services.snapshot;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.dao.snapshot.ProfileSnapshotItem;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation for Profile snapshot service
 */
@Service
public class ProfileSnapshotServiceImpl implements ProfileSnapshotService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileSnapshotServiceImpl.class);
  private ProfileSnapshotDao profileSnapshotDao;

  public ProfileSnapshotServiceImpl(@Autowired ProfileSnapshotDao profileSnapshotDao) {
    this.profileSnapshotDao = profileSnapshotDao;
  }

  @Override
  public Future<Optional<ProfileSnapshotWrapper>> getById(String id, String tenantId) {
    return profileSnapshotDao.getById(id, tenantId)
      .map(optionalWrapper ->
        optionalWrapper.isPresent() ? Optional.of(convertRootSnapshotContent(optionalWrapper.get())) : optionalWrapper);
  }

  @Override
  public Future<ProfileSnapshotWrapper> createSnapshot(String jobProfileId, String tenantId) {
    Future<ProfileSnapshotWrapper> future = Future.future();
    profileSnapshotDao.getSnapshotItems(jobProfileId, tenantId).setHandler(ar -> {
      List<ProfileSnapshotItem> snapshotItems = ar.result();
      if (snapshotItems.isEmpty()) {
        String errorMessage = "Can not build snapshot for Job Profile, probably jobProfileId is wrong: " + jobProfileId;
        LOGGER.error(errorMessage);
        future.fail(errorMessage);
      } else {
        ProfileSnapshotWrapper rootWrapper = buildSnapshot(snapshotItems);
        profileSnapshotDao.save(rootWrapper, tenantId).setHandler(savedAr -> {
          if (savedAr.failed()) {
            future.fail(savedAr.cause());
          } else {
            future.complete(rootWrapper);
          }
        });
      }
    });
    return future;
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

    ProfileSnapshotItem rootItem = snapshotItems.stream().filter(item -> item.getMasterId() == null).findFirst().get();
    ProfileSnapshotWrapper rootWrapper = new ProfileSnapshotWrapper();
    rootWrapper.setId(UUID.randomUUID().toString());
    rootWrapper.setContentType(rootItem.getDetailType());
    rootWrapper.setContent(convertContentByType(rootItem.getDetail(), rootItem.getDetailType()));
    fillChildSnapshotWrappers(rootItem.getDetailId(), rootWrapper.getChildSnapshotWrappers(), snapshotItems);
    return rootWrapper;
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
  private void fillChildSnapshotWrappers(String parentId, List<ChildSnapshotWrapper> childWrappers, List<ProfileSnapshotItem> snapshotItems) {
    for (ProfileSnapshotItem snapshotItem : snapshotItems) {
      if (parentId.equals(snapshotItem.getMasterId())) {
        ChildSnapshotWrapper childWrapper = new ChildSnapshotWrapper();
        childWrapper.setId(snapshotItem.getDetailId());
        childWrapper.setContentType(snapshotItem.getDetailType());
        childWrapper.setContent(convertContentByType(snapshotItem.getDetail(), snapshotItem.getDetailType()));
        childWrappers.add(childWrapper);
        fillChildSnapshotWrappers(childWrapper.getId(), childWrapper.getChildSnapshotWrappers(), snapshotItems);
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
    Iterator<ProfileSnapshotItem> iterator = snapshotItems.listIterator();
    while (iterator.hasNext()) {
      ProfileSnapshotItem current = iterator.next();
      if (!duplicates.add(current.getAssociationId())) {
        iterator.remove();
      }
    }
  }

  /**
   * Converts 'content' field of the given root wrapper (ProfileSnapshotWrapper) and it's child wrappers
   * to concrete Profile class. The class resolution happens by 'content type' field.
   *
   * @param wrapper the given root ProfileSnapshotWrapper
   * @return ProfileSnapshotWrapper with converted 'content' field
   */
  private ProfileSnapshotWrapper convertRootSnapshotContent(@NotNull ProfileSnapshotWrapper wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (ChildSnapshotWrapper child : wrapper.getChildSnapshotWrappers()) {
      convertChildSnapshotsContent(child);
    }
    return wrapper;
  }

  /**
   * Method converts an Object 'content' field to concrete Profile class doing the same for all the child wrappers.
   *
   * @param wrapper given child wrapper
   * @return ChildSnapshotWrapper with properly converted 'content' field
   */
  private ChildSnapshotWrapper convertChildSnapshotsContent(ChildSnapshotWrapper wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (ChildSnapshotWrapper child : wrapper.getChildSnapshotWrappers()) {
      convertChildSnapshotsContent(child);
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
    switch (contentType) {
      case JOB_PROFILE:
        return (T) new ObjectMapper().convertValue(content, JobProfile.class);
      case MATCH_PROFILE:
        return (T) new ObjectMapper().convertValue(content, MatchProfile.class);
      case ACTION_PROFILE:
        return (T) new ObjectMapper().convertValue(content, ActionProfile.class);
      case MAPPING_PROFILE:
        return (T) new ObjectMapper().convertValue(content, MappingProfile.class);
      default:
        throw new IllegalStateException("Can not find profile by snapshot content type: " + contentType.toString());
    }
  }
}
