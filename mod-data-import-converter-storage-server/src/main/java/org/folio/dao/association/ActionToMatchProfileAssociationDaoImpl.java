package org.folio.dao.association;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Component
public class ActionToMatchProfileAssociationDaoImpl extends AbstractProfileAssociationDao<ActionProfileCollection, MatchProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(ActionToMatchProfileAssociationDaoImpl.class);
  private static final String TABLE_NAME = "action_to_match_profiles";

  public ActionToMatchProfileAssociationDaoImpl(@Autowired ProfileDao<ActionProfile, ActionProfileCollection> detailProfileDao,
                                                @Autowired ProfileDao<MatchProfile, JobProfileCollection> masterProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
