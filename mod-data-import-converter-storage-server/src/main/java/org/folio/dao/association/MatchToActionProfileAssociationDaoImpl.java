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
public class MatchToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao<MatchProfileCollection, ActionProfileCollection> {
  private static final String TABLE_NAME = "match_to_action_profiles";

  public MatchToActionProfileAssociationDaoImpl(@Autowired ProfileDao<MatchProfile, MatchProfileCollection> masterProfileDao,
                                                @Autowired ProfileDao<ActionProfile, ActionProfileCollection> detailProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
