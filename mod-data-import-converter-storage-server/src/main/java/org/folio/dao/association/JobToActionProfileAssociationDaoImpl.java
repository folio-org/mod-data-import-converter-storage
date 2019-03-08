package org.folio.dao.association;

import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Component
public class JobToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao<JobProfileCollection, ActionProfileCollection> {
  private static final String TABLE_NAME = "job_to_action_profiles";

  public JobToActionProfileAssociationDaoImpl(@Autowired ProfileDao<JobProfile, JobProfileCollection> masterProfileDao,
                                              @Autowired ProfileDao<ActionProfile, ActionProfileCollection> detailProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
