package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;
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
  private static final String MATCH_TO_ACTION_ASSOCIATION_TABLE = "match_to_action_profiles";
  private static final String MATCH_TO_MATCH_ASSOCIATION_TABLE = "match_to_match_profiles";

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

  @Override
  protected Future<Boolean> deleteAllAssociationsWithDetails(Future<SQLConnection> txConnection, String masterProfileId, String tenantId) {
    return deleteAssociationsWithDetails(txConnection, masterProfileId, MATCH_TO_ACTION_ASSOCIATION_TABLE, tenantId)
      .compose(voidDeleteRes -> deleteAssociationsWithDetails(txConnection, masterProfileId, MATCH_TO_MATCH_ASSOCIATION_TABLE, tenantId));
  }
}
