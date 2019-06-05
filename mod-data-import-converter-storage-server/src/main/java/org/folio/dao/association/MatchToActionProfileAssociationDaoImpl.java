package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("MATCH_PROFILE_TO_ACTION_PROFILE")
public class MatchToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao {

  private static final String TABLE_NAME = "match_to_action_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
