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
public class MatchToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao<MatchProfileCollection, ActionProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(MatchToActionProfileAssociationDaoImpl.class);
  private static final String TABLE_NAME = "match_to_match_profiles";

  public MatchToActionProfileAssociationDaoImpl(@Autowired ProfileDao<MatchProfile, JobProfileCollection> masterProfileDao,
                                                @Autowired ProfileDao<ActionProfile, ActionProfileCollection> detailProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
