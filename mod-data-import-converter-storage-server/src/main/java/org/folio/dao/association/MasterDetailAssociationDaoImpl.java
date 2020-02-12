package org.folio.dao.association;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import org.folio.dao.PostgresClientFactory;
import org.folio.dao.sql.SelectBuilder;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;
import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.folio.dao.sql.SelectBuilder.parseQuery;
import static org.folio.dao.sql.SelectBuilder.putInQuotes;

@Repository
public class MasterDetailAssociationDaoImpl implements MasterDetailAssociationDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(MasterDetailAssociationDaoImpl.class);

  /**
   * This query selects detail profiles by master profile id.
   */
  private static final String RETRIEVES_DETAILS_SQL = "SELECT detail_id, detail_type, detail FROM associations_view";
  /**
   * This query selects master profiles by detail profile id.
   */
  private static final String RETRIEVES_MASTERS_SQL = "SELECT master_id, master_type, master FROM associations_view";

  private static final String MASTER_ID_FIELD = "master_id";
  private static final String MASTER_TYPE_FIELD = "master_type";
  private static final String MASTER_FIELD = "master";
  private static final String DETAIL_ID_FIELD = "detail_id";
  private static final String DETAIL_TYPE_FIELD = "detail_type";
  private static final String DETAIL_FIELD = "detail";

  @Autowired
  protected PostgresClientFactory pgClientFactory;

  @Override
  public Future<List<ProfileSnapshotWrapper>> getDetailProfilesByMasterId(String masterId, ContentType detailType, String query, int offset, int limit, String tenantId) {

    SelectBuilder selectBuilder = new SelectBuilder(RETRIEVES_DETAILS_SQL)
      .where()
      .equals(MASTER_ID_FIELD, putInQuotes(masterId));

    if (detailType != null) {
      selectBuilder.and().equals(DETAIL_TYPE_FIELD, putInQuotes(detailType.value()));
    }

    if (isNotBlank(query)) {
      selectBuilder.and().appendQuery(parseQuery("associations_view.detail->(0)", query));
    }

    selectBuilder.limit(limit).offset(offset);

    return select(tenantId, selectBuilder.toString()).map(this::mapToDetails);
  }

  /**
   * Maps DETAIL_TYPE_FIELD, DETAIL_FIELD, DETAIL_ID_FIELD fields from a result set.
   *
   * @param resultSet a result of a sql query.
   * @return a list of entities.
   */
  private List<ProfileSnapshotWrapper> mapToDetails(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(it -> {
        ContentType detailType = ContentType.fromValue(it.getString(DETAIL_TYPE_FIELD));
        JsonObject detail = new JsonArray(it.getString(DETAIL_FIELD)).getJsonObject(0);
        ProfileSnapshotWrapper wrapper = new ProfileSnapshotWrapper();
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
  public Future<List<ProfileSnapshotWrapper>> getMasterProfilesByDetailId(String detailId, ContentType masterType, String query, int offset, int limit, String tenantId) {

    SelectBuilder selectBuilder = new SelectBuilder(RETRIEVES_MASTERS_SQL)
      .where()
      .equals(DETAIL_ID_FIELD, putInQuotes(detailId));

    if (masterType != null) {
      selectBuilder.and().equals(MASTER_TYPE_FIELD, putInQuotes(masterType.value()));
    }

    if (isNotBlank(query)) {
      selectBuilder.and().appendQuery(parseQuery("associations_view.master->(0)", query));
    }

    selectBuilder.limit(limit).offset(offset);

    return select(tenantId, selectBuilder.toString()).map(this::mapToMasters);
  }

  /**
   * Maps MASTER_TYPE_FIELD, MASTER_FIELD, MASTER_ID_FIELD fields from a result set.
   *
   * @param resultSet a result of a sql query.
   * @return a list of entities.
   */
  private List<ProfileSnapshotWrapper> mapToMasters(ResultSet resultSet) {
    return resultSet.getRows().stream()
      .map(object -> {
        ContentType masterType = ContentType.fromValue(object.getString(MASTER_TYPE_FIELD));
        JsonObject master = new JsonArray(object.getString(MASTER_FIELD)).getJsonObject(0);
        ProfileSnapshotWrapper wrapper = new ProfileSnapshotWrapper();
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
      LOGGER.debug("Could not perform the sql query %s for the tenant %s", sql, tenantId);
      future.fail(e);
    }
    return future;
  }
}
