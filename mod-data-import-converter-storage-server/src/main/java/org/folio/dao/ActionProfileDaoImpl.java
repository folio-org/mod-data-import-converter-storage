package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;
import org.folio.dao.association.MasterToDetailAssociationDao;
import org.folio.dao.association.ProfileAssociationDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.function.Function;

import static java.lang.String.format;
import static org.folio.dao.util.DaoUtil.constructCriteria;

/**
 * Data access object for {@link ActionProfile}
 */
@Component
public class ActionProfileDaoImpl extends AbstractProfileDao<ActionProfile, ActionProfileCollection> {

  private static final String ACTION_PROFILES_TABLE_NAME = "action_profiles";
  private static final String ACTION_TO_ACTION_ASSOCIATION_TABLE = "action_to_action_profiles";
  private static final String ACTION_TO_MAPPING_ASSOCIATION_TABLE = "action_to_mapping_profiles";
  private static final String ACTION_TO_MATCH_ASSOCIATION_TABLE = "action_to_match_profiles";

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

  @Override
  protected ActionProfile markProfileEntityAsDeleted(ActionProfile profile) {
    return profile.withDeleted(true);
  }

  @Override
  protected Future<Boolean> deleteAllAssociationsWithDetails(Future<SQLConnection> txConnection, String masterProfileId, String tenantId) {
    return deleteAssociationsWithDetails(txConnection, masterProfileId, ACTION_TO_ACTION_ASSOCIATION_TABLE, tenantId)
      .compose(voidDeleteRes -> deleteAssociationsWithDetails(txConnection, masterProfileId, ACTION_TO_MAPPING_ASSOCIATION_TABLE, tenantId))
      .compose(voidDeleteRes -> deleteAssociationsWithDetails(txConnection, masterProfileId, ACTION_TO_MATCH_ASSOCIATION_TABLE, tenantId));
  }

}
