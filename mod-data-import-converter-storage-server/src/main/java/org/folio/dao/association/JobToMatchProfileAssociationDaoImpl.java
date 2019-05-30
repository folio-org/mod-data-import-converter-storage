package org.folio.dao.association;

import org.springframework.stereotype.Repository;

@Repository("JOB_PROFILE_TO_MATCH_PROFILE")
public class JobToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao {

  private static final String TABLE_NAME = "job_to_match_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
