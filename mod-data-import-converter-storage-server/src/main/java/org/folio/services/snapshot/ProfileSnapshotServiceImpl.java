package org.folio.services.snapshot;

import io.vertx.core.Future;
import org.codehaus.jackson.map.ObjectMapper;
import org.folio.dao.snapshot.ProfileSnapshotDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.Child;
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
    for (Child child : wrapper.getChildren()) {
      convertChild(child);
    }
    return wrapper;
  }

  private Child convertChild(Child wrapper) {
    wrapper.setContent(convertContentByType(wrapper.getContent(), wrapper.getContentType()));
    for (Child child : wrapper.getChildren()) {
      convertChild(child);
    }
    return wrapper;
  }

  private <T> T convertContentByType(Object content, ProfileSnapshotWrapper.ContentType contentType) {
    if (ProfileSnapshotWrapper.ContentType.JOB_PROFILE.equals(contentType)) {
      return (T) new ObjectMapper().convertValue(content, JobProfile.class);
    }
    if (ProfileSnapshotWrapper.ContentType.ACTION_PROFILE.equals(contentType)) {
      return (T) new ObjectMapper().convertValue(content, ActionProfile.class);
    }
    if (ProfileSnapshotWrapper.ContentType.MATCH_PROFILE.equals(contentType)) {
      return (T) new ObjectMapper().convertValue(content, MatchProfile.class);
    }
    if (ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE.equals(contentType)) {
      return (T) new ObjectMapper().convertValue(content, MappingProfile.class);
    }
    throw new IllegalStateException();
  }
}
