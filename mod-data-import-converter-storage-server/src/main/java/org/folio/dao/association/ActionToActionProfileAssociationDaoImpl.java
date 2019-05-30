package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("ACTION_PROFILE_TO_ACTION_PROFILE")
public class ActionToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao {
  private static final String TABLE_NAME = "action_to_action_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
