package org.folio.dao.association;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dao.ProfileDao;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileCollection;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link ProfileAssociationDao}
 */
@Component
public class ActionToMappingProfileAssociationDaoImpl extends AbstractProfileAssociationDao<ActionProfileCollection, MappingProfileCollection> {

  private static final Logger logger = LoggerFactory.getLogger(ActionToMappingProfileAssociationDaoImpl.class);
  private static final String TABLE_NAME = "action_to_mapping_profiles";

  public ActionToMappingProfileAssociationDaoImpl(@Autowired ProfileDao<ActionProfile, ActionProfileCollection> masterProfileDao,
                                                  @Autowired ProfileDao<MappingProfile, MappingProfileCollection> detailProfileDao) {
    super(masterProfileDao, detailProfileDao);
  }

  @Override
  String getTableName() {
    return TABLE_NAME;
  }
}
