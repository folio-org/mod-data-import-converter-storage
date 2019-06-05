package org.folio.dao.association;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.folio.dao.sql.SelectBuilder.parseQuery;
import static org.folio.dao.sql.SelectBuilder.putInQuotes;
import static org.folio.dao.util.DaoUtil.constructCriteria;
import static org.folio.dao.util.DaoUtil.getCQLWrapper;

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
import org.folio.dao.sql.SelectBuilder;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ChildSnapshotWrapper;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileAssociation;
import org.folio.rest.jaxrs.model.ProfileAssociationCollection;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic implementation of the of the {@link ProfileAssociationDao}
 *
 */
public abstract class AbstractProfileAssociationDao implements ProfileAssociationDao {
  private static final String ID_FIELD = "'id'";
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProfileAssociationDao.class);

  @Autowired
  protected PostgresClientFactory pgClientFactory;

  @Override
  public Future<String> save(ProfileAssociation entity, String tenantId) {
    Future<String> future = Future.future();
    pgClientFactory.createInstance(tenantId).save(getTableName(), entity.getId(), entity, future.completer());
    return future;
  }

  @Override
  public Future<ProfileAssociationCollection> getAll(String tenantId) {
    Future<Results<ProfileAssociation>> future = Future.future();
    try {
      String[] fieldList = {"*"};
      pgClientFactory.createInstance(tenantId).get(getTableName(), ProfileAssociation.class, fieldList, null, true, future.completer());
    } catch (Exception e) {
      LOGGER.error("Error while searching for ProfileAssociations", e);
      future.fail(e);
    }
    return future.map(profileAssociationResults -> new ProfileAssociationCollection()
      .withProfileAssociations(profileAssociationResults.getResults())
      .withTotalRecords(profileAssociationResults.getResultInfo().getTotalRecords()));
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



  /**
   * Provides access to the table name
   *
   * @return table name
   */
  abstract String getTableName();
}
