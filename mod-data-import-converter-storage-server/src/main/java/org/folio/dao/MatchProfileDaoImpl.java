package org.folio.dao;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link MatchProfile}
 */
@Component
public class MatchProfileDaoImpl extends AbstractProfileDao<MatchProfile, MatchProfileCollection> {

  private static final String MATCH_PROFILES_TABLE_NAME = "match_profiles";

  @Override
  String getTableName() {
    return MATCH_PROFILES_TABLE_NAME;
  }

  @Override
  Future<MatchProfileCollection> mapResultsToCollection(Future<Results<MatchProfile>> resultsFuture) {
    return resultsFuture.map(results -> new MatchProfileCollection()
      .withMatchProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  Class<MatchProfile> getProfileType() {
    return MatchProfile.class;
  }

  @Override
  String getProfileId(MatchProfile profile) {
    return profile.getId();
  }

  @Override
  protected MatchProfile markProfileEntityAsDeleted(MatchProfile profile) {
    return profile.withDeleted(true);
  }
}
