package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("MATCH_PROFILE_TO_MATCH_PROFILE")
public class MatchToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao {

  private static final String TABLE_NAME = "match_to_match_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
