package org.folio.dao;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileCollection;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.stereotype.Component;

/**
 * Data access object for {@link MappingProfile}
 */
@Component
public class MappingProfileDaoImpl extends AbstractProfileDao<MappingProfile, MappingProfileCollection> {

  private static final String MAPPING_PROFILES_TABLE_NAME = "mapping_profiles";

  @Override
  String getTableName() {
    return MAPPING_PROFILES_TABLE_NAME;
  }

  @Override
  Future<MappingProfileCollection> mapResultsToCollection(Future<Results<MappingProfile>> resultsFuture) {
    return resultsFuture.map(results -> new MappingProfileCollection()
      .withMappingProfiles(results.getResults())
      .withTotalRecords(results.getResultInfo().getTotalRecords()));
  }

  @Override
  Class<MappingProfile> getProfileType() {
    return MappingProfile.class;
  }

  @Override
  String getProfileId(MappingProfile profile) {
    return profile.getId();
  }

  @Override
  protected MappingProfile markProfileEntityAsDeleted(MappingProfile profile) {
    return profile.withDeleted(true);
  }
}
