package org.folio.dao;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ActionProfile}
 */
@Component
public class ActionProfileDaoImpl extends AbstractProfileDao<ActionProfile, ActionProfileCollection> {

  private static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";

  @Override
  String getTableName() {
    return ACTION_PROFILES_TABLE_NAME;
  }

  @Override
  Future<ActionProfileCollection> mapResultsToCollection(Future<Results<ActionProfile>> resultsFuture) {
    return resultsFuture.map(results -> new ActionProfileCollection()
      .withActionProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  Class<ActionProfile> getProfileType() {
    return ActionProfile.class;
  }

  @Override
  String getProfileId(ActionProfile profile) {
    return profile.getId();
  }
}
