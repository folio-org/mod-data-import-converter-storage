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
        optionalWrapper.isPresent() ? Optional.of(convertWrapper(optionalWrapper.get())) : optionalWrapper);
  }

  private ProfileSnapshotWrapper convertWrapper(@NotNull ProfileSnapshotWrapper wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (ChildSnapshotWrapper child : wrapper.getChildSnapshotWrappers()) {
      convertChild(child);
    }
    return wrapper;
  }

  private ChildSnapshotWrapper convertChild(ChildSnapshotWrapper wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (ChildSnapshotWrapper child : wrapper.getChildSnapshotWrappers()) {
      convertChild(child);
    }
    return wrapper;
  }

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
