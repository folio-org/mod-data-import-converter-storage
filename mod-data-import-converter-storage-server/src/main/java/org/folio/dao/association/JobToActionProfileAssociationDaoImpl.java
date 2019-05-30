package org.folio.dao.association;

import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Repository("JOB_PROFILE_TO_ACTION_PROFILE")
public class JobToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao {
  private static final String TABLE_NAME = "job_to_action_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
