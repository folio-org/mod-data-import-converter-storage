package org.folio.services;

import io.vertx.core.Future;
import org.folio.dataimport.util.OkapiConnectionParams;
import org.folio.rest.jaxrs.model.DataType;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JobProfileServiceImpl extends AbstractProfileService<JobProfile, JobProfileCollection> {

  @Override
  public Future<JobProfile> saveProfile(JobProfile profile, OkapiConnectionParams params) {
    profile.setDataTypes(sortDataTypes(profile.getDataTypes()));
    return super.saveProfile(profile, params);
  }

  @Override
  public Future<JobProfile> updateProfile(JobProfile profile, OkapiConnectionParams params) {
    profile.setDataTypes(sortDataTypes(profile.getDataTypes()));
    return super.updateProfile(profile, params);
  }

  @Override
  JobProfile setProfileId(JobProfile profile) {
    return profile.withId(UUID.randomUUID().toString());
  }

  @Override
  Future<JobProfile> setUserInfoForProfile(JobProfile profile, OkapiConnectionParams params) {
    return lookupUser(profile.getMetadata().getUpdatedByUserId(), params)
      .compose(userInfo -> Future.succeededFuture(profile.withUserInfo(userInfo)));
  }

  private List<DataType> sortDataTypes(List<DataType> list) {
    if (list == null) {
      return Collections.emptyList();
    }
    Collections.sort(list);
    return list;
  }
}
