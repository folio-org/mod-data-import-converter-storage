package org.folio.dao.association;

import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Repository
public class MatchToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao<MatchProfileCollection, MatchProfileCollection> {
  private static final String TABLE_NAME = "match_to_match_profiles";

  public MatchToMatchProfileAssociationDaoImpl(@Autowired ProfileDao<MatchProfile, MatchProfileCollection> profileDao) {
    super(profileDao, profileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
