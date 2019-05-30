package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("ACTION_PROFILE_TO_MAPPING_PROFILE")
public class ActionToMappingProfileAssociationDaoImpl extends AbstractProfileAssociationDao {

  private static final String TABLE_NAME = "action_to_mapping_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
