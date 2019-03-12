package org.folio.dao.association;

import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.JobProfileCollection;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Repository
public class JobToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao<JobProfileCollection, MatchProfileCollection> {
  private static final String TABLE_NAME = "job_to_match_profiles";

  public JobToMatchProfileAssociationDaoImpl(@Autowired ProfileDao<JobProfile, JobProfileCollection> masterProfileDao,
                                             @Autowired ProfileDao<MatchProfile, MatchProfileCollection> detailProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
