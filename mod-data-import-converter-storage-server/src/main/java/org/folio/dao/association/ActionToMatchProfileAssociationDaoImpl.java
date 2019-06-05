package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("ACTION_PROFILE_TO_MATCH_PROFILE")
public class ActionToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao {
  private static final String TABLE_NAME = "action_to_match_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
