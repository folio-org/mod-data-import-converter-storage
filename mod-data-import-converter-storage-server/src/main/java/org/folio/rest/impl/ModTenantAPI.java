package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

  private static final Logger LOGGER = LogManager.getLogger();
  private static final String DEFAULT_JOB_PROFILE_SQL = "templates/db_scripts/defaultData/default_job_profile.sql";
  private static final String DEFAULT_OCLC_JOB_PROFILE_SQL = "templates/db_scripts/defaultData/default_oclc_job_profile.sql";
  private static final String DEFAULT_OCLC_UPDATE_JOB_PROFILE_SQL = "templates/db_scripts/defaultData/default_oclc_update_job_profile.sql";
  private static final String DEFAULT_MARC_FIELD_PROTECTION_SETTINGS_SQL = "templates/db_scripts/defaultData/default_marc_field_protection_settings.sql";
  private static final String TENANT_PLACEHOLDER = "${myuniversity}";
  private static final String MODULE_PLACEHOLDER = "${mymodule}";

  @Validate
  @Override
  public void postTenant(TenantAttributes entity, Map<String, String> headers, Handler<AsyncResult<Response>> handlers, Context context) {
    super.postTenant(entity, headers, ar -> {
      if (ar.failed()) {
        handlers.handle(ar);
      } else {
        setupDefaultData(DEFAULT_JOB_PROFILE_SQL, headers, context)
          .compose(r -> setupDefaultData(DEFAULT_MARC_FIELD_PROTECTION_SETTINGS_SQL, headers, context))
          .compose(d -> setupDefaultData(DEFAULT_OCLC_JOB_PROFILE_SQL, headers, context))
          .compose(u -> setupDefaultData(DEFAULT_OCLC_UPDATE_JOB_PROFILE_SQL, headers, context))
          .onComplete(event -> handlers.handle(ar));
      }
    }, context);
  }

  private Future<List<String>> setupDefaultData(String script, Map<String, String> headers, Context context) {
    try {
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(script);

      if (inputStream == null) {
        LOGGER.info("Default data was not initialized: no resources found: {}", script);
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

      return promise.future();
    } catch (IOException e) {
      LOGGER.error("Failed to initialize default data", e);
      return Future.failedFuture(e);
    }
  }

}
