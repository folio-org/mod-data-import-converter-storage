package org.folio.dao.association;

import static java.lang.String.format;
import static org.folio.dao.util.DaoUtil.constructCriteria;

import javax.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import org.folio.dao.PostgresClientFactory;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic implementation of the of the {@link ProfileAssociationDao}
 *
 * @param <M> entity data type of the 'master' profile
 * @param <D> entity data type of the 'detail' profile
 */
public abstract class AbstractProfileAssociationDao<M, D> implements ProfileAssociationDao<M, D> {
  private static final String ID_FIELD = "'id'";
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProfileAssociationDao.class);
  /**
   * This query selects detail profiles by master profile id.
   */
  private static final String RETRIEVES_DETAILS_SQL = "SELECT detail_id, detail_type, detail FROM associations_view WHERE master_id='%s'";
  /**
   * This query selects master profiles by detail profile id.
   */
  private static final String RETRIEVES_MASTERS_SQL = "SELECT master_id, master_type, master FROM associations_view WHERE detail_id='%s'";

  private static final String MASTER_ID_FIELD = "master_id";
  private static final String MASTER_TYPE_FIELD = "master_type";
  private static final String MASTER_FIELD = "master";
  private static final String DETAIL_ID_FIELD = "detail_id";
  private static final String DETAIL_TYPE_FIELD = "detail_type";
  private static final String DETAIL_FIELD = "detail";


  @Autowired
  protected PostgresClientFactory pgClientFactory;

  @Override
  public Future<String> save(ProfileAssociation entity, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(getTableName(), entity.getId(), entity, future.completer());
    return future;
  }

  @Override
  public Future<Optional<ProfileAssociation>> getById(String id, String tenantId) {
    Future<Results<ProfileAssociation>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, id);
      pgClientFactory.createInstance(tenantId).get(getTableName(), ProfileAssociation.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      LOGGER.error("Error querying {} by id", ProfileAssociation.class.getSimpleName(), e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(profiles -> profiles.isEmpty() ? Optional.empty() : Optional.of(profiles.get(0)));
  }

  @Override
  public Future<ProfileAssociation> update(ProfileAssociation entity, String tenantId) {
    Future<ProfileAssociation> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, entity.getId());
      pgClientFactory.createInstance(tenantId).update(getTableName(), entity, new Criterion(idCrit), true, updateResult -> {
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
      LOGGER.error("Error updating {} with id {}", ProfileAssociation.class, entity.getId(), e);
      future.fail(e);
    }
    return future;
  }

  @Override
  public Future<Boolean> delete(String id, String tenantId) {
    Future<UpdateResult> future = Future.future();
    pgClientFactory.createInstance(tenantId).delete(getTableName(), id, future.completer());
    return future.map(updateResult -> updateResult.getUpdated() == 1);
  }

  @Override
  public Future<List<ChildSnapshotWrapper>> getDetailProfilesByMasterId(String masterId, String tenantId) {
    return select(tenantId, format(RETRIEVES_DETAILS_SQL, masterId)).map(this::mapToDetails);
  }

  /**
   * Maps DETAIL_TYPE_FIELD, DETAIL_FIELD, DETAIL_ID_FIELD fields from a result set.
   *
   * @param resultSet a result of a sql query.
   * @return a list of entities.
   */
  private List<ChildSnapshotWrapper> mapToDetails(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(it -> {
        ContentType detailType = ContentType.fromValue(it.getString(DETAIL_TYPE_FIELD));
        JsonObject detail = new JsonArray(it.getString(DETAIL_FIELD)).getJsonObject(0);
        ChildSnapshotWrapper wrapper = new ChildSnapshotWrapper();
        wrapper.setId(it.getString(DETAIL_ID_FIELD));
        wrapper.setContentType(detailType);
        wrapper.setContent(mapProfile(detail, detailType));
        return wrapper;
      }).collect(Collectors.toList());
  }

  /**
   * Maps json object to a profile class based on type of a profile.
   *
   * @param object      json object that represent a profile.
   * @param contentType type of a profile.
   * @return a profile instance.
   */
  private Object mapProfile(JsonObject object, ContentType contentType) {
    switch (contentType) {
      case JOB_PROFILE:
        return object.mapTo(JobProfile.class);
      case MATCH_PROFILE:
        return object.mapTo(MatchProfile.class);
      case ACTION_PROFILE:
        return object.mapTo(ActionProfile.class);
      case MAPPING_PROFILE:
        return object.mapTo(MappingProfile.class);
      default:
        throw new IllegalStateException("Can not find profile by content type: " + contentType.toString());
    }
  }


  @Override
  public Future<List<ChildSnapshotWrapper>> getMasterProfilesByDetailId(String detailId, String tenantId) {
    return select(tenantId, format(RETRIEVES_MASTERS_SQL, detailId)).map(this::mapToMasters);
  }

  /**
   * Maps MASTER_TYPE_FIELD, MASTER_FIELD, MASTER_ID_FIELD fields from a result set.
   *
   * @param resultSet a result of a sql query.
   * @return a list of entities.
   */
  private List<ChildSnapshotWrapper> mapToMasters(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(object -> {
        ContentType masterType = ContentType.fromValue(object.getString(MASTER_TYPE_FIELD));
        JsonObject master = new JsonArray(object.getString(MASTER_FIELD)).getJsonObject(0);
        ChildSnapshotWrapper wrapper = new ChildSnapshotWrapper();
        wrapper.setId(object.getString(MASTER_ID_FIELD));
        wrapper.setContentType(masterType);
        wrapper.setContent(mapProfile(master, masterType));
        return wrapper;
      }).collect(Collectors.toList());
  }

  /**
   * Selects a sql query.
   *
   * @param tenantId a tenant id.
   * @param sql      a sql query.
   * @return a result set of a query.
   */
  private Future<ResultSet> select(String tenantId, String sql) {
    Future<ResultSet> future = Future.future();
    try {
      pgClientFactory.createInstance(tenantId).select(sql, future.completer());
    } catch (Exception e) {
      LOGGER.debug("I could not perform the sql query %s for the tenant %s", sql, tenantId);
      future.fail(e);
    }
    return future;
  }

  /**
   * Provides access to the table name
   *
   * @return table name
   */
  abstract String getTableName();
}
