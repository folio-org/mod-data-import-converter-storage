package org.folio.dao.association;

import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Component
public class ActionToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao<ActionProfileCollection, MatchProfileCollection> {
  private static final String TABLE_NAME = "action_to_match_profiles";

  public ActionToMatchProfileAssociationDaoImpl(@Autowired ProfileDao<ActionProfile, ActionProfileCollection> detailProfileDao,
                                                @Autowired ProfileDao<MatchProfile, MatchProfileCollection> masterProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
