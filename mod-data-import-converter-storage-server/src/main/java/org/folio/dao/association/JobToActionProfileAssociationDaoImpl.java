package org.folio.dao.association;

import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Repository
public class JobToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao<JobProfileCollection, ActionProfileCollection> {
  private static final String TABLE_NAME = "job_to_action_profiles";

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
