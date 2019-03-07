package org.folio.dao.association;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Component
public class ActionToActionProfileAssociationDaoImpl extends AbstractProfileAssociationDao<ActionProfileCollection, ActionProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(ActionToActionProfileAssociationDaoImpl.class);
  private static final String TABLE_NAME = "match_to_match_profiles";

  public ActionToActionProfileAssociationDaoImpl(@Autowired ProfileDao<ActionProfile, ActionProfileCollection> profileDao) {
    super(profileDao, profileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
