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
  protected static final String ID_FIELD = "'id'";
  private static final Logger logger = LoggerFactory.getLogger(AbstractProfileAssociationDao.class);
  private static final String RETRIEVES_DETAILS_SQL = "Select * from associations_view Where master_id='%s'";
  private static final String RETRIEVES_MASTERS_SQL = "Select * from associations_view Where detail_id='%s'";

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
      logger.error("Error querying {} by id", ProfileAssociation.class.getSimpleName(), e);
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
          logger.error("Could not update {} with id {}", ProfileAssociation.class, entity.getId(), updateResult.cause());
          future.fail(updateResult.cause());
        } else if (updateResult.result().getUpdated() != 1) {
          String errorMessage = format("%s with id '%s' was not found", ProfileAssociation.class, entity.getId());
          logger.error(errorMessage);
          future.fail(new NotFoundException(errorMessage));
        } else {
          future.complete(entity);
        }
      });
    } catch (Exception e) {
      logger.error("Error updating {} with id {}", ProfileAssociation.class, entity.getId(), e);
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
  public Future<List<ChildSnapshotWrapper>> getDetailProfilesByMasterId(String tenantId, String masterId) {
    return select(tenantId, format(RETRIEVES_DETAILS_SQL, masterId)).map(this::mapToDetails);
  }

  private List<ChildSnapshotWrapper> mapToDetails(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(object -> {
        ChildSnapshotWrapper wrapper = new ChildSnapshotWrapper();
        wrapper.setId(object.getString("detail_id"));
        ContentType detailType = ContentType.fromValue(object.getString("detail_type"));
        wrapper.setContentType(detailType);
        JsonObject detail = new JsonArray(object.getString("detail")).getJsonObject(0);
        wrapper.setContent(mapProfile(detail, detailType));
        return wrapper;
      }).collect(Collectors.toList());
  }

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
  public Future<List<ChildSnapshotWrapper>> getMasterProfilesByDetailId(String tenantId, String detailId) {
    return select(tenantId, format(RETRIEVES_MASTERS_SQL, detailId)).map(this::mapToMasters);
  }

  private List<ChildSnapshotWrapper> mapToMasters(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(object -> {
        ChildSnapshotWrapper wrapper = new ChildSnapshotWrapper();
        wrapper.setId(object.getString("master_id"));
        ContentType masterType = ContentType.fromValue(object.getString("master_type"));
        wrapper.setContentType(masterType);
        JsonObject detail = new JsonArray(object.getString("master")).getJsonObject(0);
        wrapper.setContent(mapProfile(detail, masterType));
        return wrapper;
      }).collect(Collectors.toList());
  }

  private Future<ResultSet> select(String tenantId, String sql) {
    Future<ResultSet> future = Future.future();
    try {
      pgClientFactory.createInstance(tenantId).select(sql, future.completer());
    } catch (Exception e) {
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
