package org.folio.services;

import io.vertx.core.Future;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MatchProfileServiceImpl implements ProfileService<MatchProfile, MatchProfileCollection> {

  @Autowired
  private ProfileDao<MatchProfile, MatchProfileCollection> matchProfileDao;

  @Override
  public Future<MatchProfileCollection> getProfiles(String query, int offset, int limit, String tenantId) {
    return matchProfileDao.getProfiles(query, offset, limit, tenantId);
  }

  @Override
  public Future<Optional<MatchProfile>> getProfileById(String id, String tenantId) {
    return matchProfileDao.getProfileById(id, tenantId);
  }

  @Override
  public Future<MatchProfile> saveProfile(MatchProfile profile, String tenantId) {
    profile.setId(UUID.randomUUID().toString());
    return matchProfileDao.saveProfile(profile, tenantId).map(profile);
  }

  @Override
  public Future<MatchProfile> updateProfile(MatchProfile profile, String tenantId) {
    return matchProfileDao.updateProfile(profile, tenantId);
  }

  @Override
  public Future<Boolean> deleteProfile(String id, String tenantId) {
    return matchProfileDao.deleteProfile(id, tenantId);
  }
}
