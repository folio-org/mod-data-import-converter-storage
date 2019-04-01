package org.folio.services.snapshot;

import io.vertx.core.Future;
import org.codehaus.jackson.map.ObjectMapper;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class ProfileSnapshotServiceImpl implements ProfileSnapshotService {

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
