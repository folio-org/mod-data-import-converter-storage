package org.folio.dao.association;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.UpdateResult;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileAssociationCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.folio.dao.util.DaoUtil.constructCriteria;
import static org.folio.dataimport.util.DaoUtil.getCQLWrapper;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.ACTION_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.JOB_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MAPPING_PROFILE;
import static org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType.MATCH_PROFILE;

/**
 * Generic implementation of the of the {@link ProfileAssociationDao}
 */
@Repository
@SuppressWarnings("squid:CallToDeprecatedMethod")
public class CommonProfileAssociationDao implements ProfileAssociationDao {
  private static final String ID_FIELD = "'id'";
  private static final String MASTER_ID_FIELD = "masterProfileId";
  private static final String DETAIL_ID_FIELD = "detailProfileId";
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonProfileAssociationDao.class);
  private static final String CORRECT_PROFILE_ASSOCIATION_TYPES_MESSAGE = "Correct ProfileAssociation types: " +
    "ACTION_PROFILE_TO_ACTION_PROFILE, " +
    "ACTION_PROFILE_TO_MAPPING_PROFILE, " +
    "ACTION_PROFILE_TO_MATCH_PROFILE, " +
    "JOB_PROFILE_TO_ACTION_PROFILE, " +
    "JOB_PROFILE_TO_MATCH_PROFILE, " +
    "MATCH_PROFILE_TO_ACTION_PROFILE, " +
    "MATCH_PROFILE_TO_MATCH_PROFILE";
  private static Map<String, String> associationTableNamesMap;
  @Autowired
  protected PostgresClientFactory pgClientFactory;

  static {
    associationTableNamesMap = new HashMap<>();
    associationTableNamesMap.put(ACTION_PROFILE.value() + ACTION_PROFILE.value(), "action_to_action_profiles");
    associationTableNamesMap.put(ACTION_PROFILE.value() + MAPPING_PROFILE.value(), "action_to_mapping_profiles");
    associationTableNamesMap.put(ACTION_PROFILE.value() + MATCH_PROFILE.value(), "action_to_match_profiles");
    associationTableNamesMap.put(JOB_PROFILE.value() + ACTION_PROFILE.value(), "job_to_action_profiles");
    associationTableNamesMap.put(JOB_PROFILE.value() + MATCH_PROFILE.value(), "job_to_match_profiles");
    associationTableNamesMap.put(MATCH_PROFILE.value() + ACTION_PROFILE.value(), "match_to_action_profiles");
    associationTableNamesMap.put(MATCH_PROFILE.value() + MATCH_PROFILE.value(), "match_to_match_profiles");
  }

  @Override
  public Future<String> save(ProfileAssociation entity, ContentType masterType, ContentType detailType, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(getAssociationTableName(masterType, detailType), entity.getId(), entity, future.completer());
    return future;
  }

  @Override
  public Future<ProfileAssociationCollection> getAll(ContentType masterType, ContentType detailType, String tenantId) {
    Future<Results<ProfileAssociation>> future = Future.future();
    try {
      String[] fieldList = {"*"};
      pgClientFactory.createInstance(tenantId).get(getAssociationTableName(masterType, detailType), ProfileAssociation.class, fieldList, null, true, future.completer());
    } catch (Exception e) {
      LOGGER.error("Error while searching for ProfileAssociations", e);
      future.fail(e);
    }
    return future.map(profileAssociationResults -> new ProfileAssociationCollection()
      .withProfileAssociations(profileAssociationResults.getResults())
      .withTotalRecords(profileAssociationResults.getResultInfo().getTotalRecords()));
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, ContentType masterType, ContentType detailType, String tenantId) {
    Future<Results<ProfileAssociation>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(getAssociationTableName(masterType, detailType), ProfileAssociation.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      LOGGER.error("Error querying {} by id", e, ProfileAssociation.class.getSimpleName());
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(profiles -> profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0)));
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, ProfileSnapshotWrapper.ContentType masterType, ProfileSnapshotWrapper.ContentType detailType, String tenantId) {
    Future<ProfileAssociation> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, entity.getId());
      pgClientFactory.createInstance(tenantId).update(getAssociationTableName(masterType, detailType), entity, new Criterion(idCrit), true, updateResult -> {
        if (updateResult.failed()) {
          LOGGER.error("Could not update {} with id {}", ProfileAssociation.class, entity.getId(), updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = format("%s with id '%s' was not found", ProfileAssociation.class, entity.getId());
          LOGGER.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(entity);
        }
      });
    } catch (Exception e) {
      LOGGER.error("Error updating {} with id {}", e, ProfileAssociation.class, entity.getId());
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<Boolean> delete(String id, ProfileSnapshotWrapper.ContentType masterType, ProfileSnapshotWrapper.ContentType detailType, String tenantId) {
    Future<UpdateResult> future = Future.future();
    pgClientFactory.createInstance(tenantId).delete(getAssociationTableName(masterType, detailType), id, future.completer());
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }

  @Override
  public Future<Boolean> delete(String masterId, String detailId, ProfileSnapshotWrapper.ContentType masterType, ProfileSnapshotWrapper.ContentType detailType, String tenantId) {
    Future<UpdateResult> future = Future.future();
    try {
      CQLWrapper filter = getCQLWrapper(getAssociationTableName(masterType, detailType), "(" + MASTER_ID_FIELD + "==" + masterId + " AND " + DETAIL_ID_FIELD + "==" + detailId);
      pgClientFactory.createInstance(tenantId).delete(getAssociationTableName(masterType, detailType), filter, future.completer());
    } catch (Exception e) {
      return Future.failedFuture(e);
    }
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }

  /**
   * Returns association table name by masterType and detailType
   *
   * @param masterType a master type in association
   * @param detailType a detail type in association
   * @return table name
   */
  private String getAssociationTableName(ContentType masterType, ContentType detailType) {
    String associationTableName = associationTableNamesMap.get(masterType.value() + detailType.value());
    if (associationTableName == null) {
      String message = format("Invalid ProfileAssociation type with master type '%s' and detail type '%s'. ", masterType, detailType);
      LOGGER.error(message);
      throw new BadRequestException(CORRECT_PROFILE_ASSOCIATION_TYPES_MESSAGE);
    }
    return associationTableName;
  }
}
