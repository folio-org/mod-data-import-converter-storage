package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.folio.rest.annotations.Validate;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ModTenantAPI extends TenantAPI {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModTenantAPI.class);
  private static final String LOAD_SAMPLE_PARAMETER = "loadSample";
  private static final String TEST_JOB_PROFILES_SQL = "templates/db_scripts/testData/test_job_profiles.sql";
  private static final String TEST_MATCHING_PROFILES_SQL = "templates/db_scripts/testData/test_matching_profiles.sql";
  private static final String TEST_ACTION_PROFILES_SQL = "templates/db_scripts/testData/test_action_profiles.sql";
  private static final String TEST_MAPPING_PROFILES_SQL = "templates/db_scripts/testData/test_mapping_profiles.sql";
  private static final String TEST_PROFILE_ASSOCIATIONS_SQL = "templates/db_scripts/testData/test_profile_associations.sql";
  private static final String DEFAULT_JOB_PROFILE_SQL = "templates/db_scripts/testData/default_job_profile.sql";
  private static final String TENANT_PLACEHOLDER = "${myuniversity}";
  private static final String MODULE_PLACEHOLDER = "${mymodule}";

  @Validate
  @Override
  public void postTenant(TenantAttributes entity, Map<String, String> headers, Handler<AsyncResult<Response>> handlers, Context context) {
    super.postTenant(entity, headers, ar -> {
      if (ar.failed()) {
        handlers.handle(ar);
      } else {
        Future<List<String>> sampleData = setupTestData(DEFAULT_JOB_PROFILE_SQL, headers, context);
        if (!isLoadSample(entity)) {
          LOGGER.debug("Test data will not be initialized.");
          sampleData.onComplete(event -> handlers.handle(ar));
        } else {
          sampleData.compose(event -> setupTestData(TEST_JOB_PROFILES_SQL, headers, context))
            .compose(event -> setupTestData(TEST_MATCHING_PROFILES_SQL, headers, context))
            .compose(event -> setupTestData(TEST_ACTION_PROFILES_SQL, headers, context))
            .compose(event -> setupTestData(TEST_MAPPING_PROFILES_SQL, headers, context))
            .compose(event -> setupTestData(TEST_PROFILE_ASSOCIATIONS_SQL, headers, context))
            .onComplete(event -> handlers.handle(ar));
        }
      }
    }, context);
  }

  private Future<List<String>> setupTestData(String script, Map<String, String> headers, Context context) {
    try {
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(script);

      if (inputStream == null) {
        LOGGER.info("Module is being deployed in test mode, but test data was not initialized: no resources found: {}", script);
        return Future.succeededFuture();
      }

      String sqlScript = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
      if (StringUtils.isBlank(sqlScript)) {
        return Future.succeededFuture();
      }

      String tenantId = TenantTool.calculateTenantId(headers.get("x-okapi-tenant"));
      String moduleName = PostgresClient.getModuleName();

      sqlScript = sqlScript.replace(TENANT_PLACEHOLDER, tenantId).replace(MODULE_PLACEHOLDER, moduleName);

      Promise<List<String>> promise = Promise.promise();
      PostgresClient.getInstance(context.owner()).runSQLFile(sqlScript, false, promise);

      LOGGER.info("Module is being deployed in test mode, test data will be initialized. Check the server log for details.");

      return promise.future();
    } catch (IOException e) {
      return Future.failedFuture(e);
    }
  }

  private boolean isLoadSample(TenantAttributes attributes) {
    if (attributes == null) {
      return false;
    }
    return attributes.getParameters()
      .stream()
      .anyMatch(parameter -> parameter.getKey().equals(LOAD_SAMPLE_PARAMETER)
        && Boolean.parseBoolean(parameter.getValue()));
  }

}
