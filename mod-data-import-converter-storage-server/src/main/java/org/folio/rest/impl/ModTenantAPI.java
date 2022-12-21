package org.folio.rest.impl;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.tools.utils.TenantTool;

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
  private static final String DEFAULT_QM_INSTANCE_AND_SRS_MARC_BIB_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_qm_instance_and_srs_marc_bib_create_job_profile.sql";
  private static final String DEFAULT_QM_HOLDINGS_AND_SRS_MARC_HOLDINGS_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_qm_holdings_and_srs_marc_holdings_create_job_profile.sql";
  private static final String UPDATE_DEFAULT_QM_INSTANCE_AND_SRS_MARC_BIB_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_update_qm_instance_and_srs_marc_bib_create_job_profile.sql";
  private static final String DEFAULT_INSTANCE_AND_MARC_BIB_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_instance_and_marc_bib_create_job_profile.sql";
  private static final String DEFAULT_EDIFACT_MAPPING_PROFILES = "templates/db_scripts/defaultData/default_edifact_mapping_profiles.sql";
  private static final String DEFAULT_MARC_AUTHORITY_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_marc_authority_job_profile.sql";
  private static final String DEFAULT_MARC_HOLDINGS_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_marc_holdings_job_profile.sql";
  private static final String DEFAULT_UPDATE_MARC_AUTHORITY_JOB_PROFILE = "templates/db_scripts/defaultData/default_update_marc_authority_job_profile.sql";
  private static final String DEFAULT_UPDATE_MARC_HOLDINGS_JOB_PROFILE = "templates/db_scripts/defaultData/default_update_marc_holdings_job_profile.sql";
  private static final String DEFAULT_UPDATE_QM_SRS_MARC_HOLDINGS_JOB_PROFILE = "templates/db_scripts/defaultData/default_update_qm_holdings_and_srs_marc_holdings_create_job_profile.sql";
  private static final String DEFAULT_UPDATE_INSTANCE_AND_MARC_BIB_CREATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_update_instance_and_marc_bib_create_job_profile.sql";
  private static final String DEFAULT_UPDATE_OCLC_JOB_PROFILE_SQL = "templates/db_scripts/defaultData/default_update_oclc_job_profile.sql";
  private static final String DEFAULT_UPDATE_OCLC_UPDATE_JOB_PROFILE_SQL = "templates/db_scripts/defaultData/default_update_oclc_update_job_profile.sql";
  private static final String DEFAULT_UPDATE_EDIFACT_MAPPING_PROFILES = "templates/db_scripts/defaultData/default_update_edifact_mapping_profiles.sql";
  private static final String DEFAULT_DELETE_MARC_AUTHORITY_JOB_PROFILES = "templates/db_scripts/defaultData/default_delete_marc_authority_job_profile.sql";
  private static final String DEFAULT_QM_AUTHORITY_UPDATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_qm_authority_update_job_profile.sql";
  private static final String DEFAULT_QM_MARC_BIB_UPDATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_qm_marc_bib_update_job_profile.sql";
  private static final String DEFAULT_QM_HOLDINGS_UPDATE_JOB_PROFILE = "templates/db_scripts/defaultData/default_qm_holdings_update_job_profile.sql";

  private static final String TENANT_PLACEHOLDER = "${myuniversity}";
  private static final String MODULE_PLACEHOLDER = "${mymodule}";

  @Override
  Future<Integer> loadData(TenantAttributes attributes, String tenantId, Map<String, String> headers, Context context) {
    return super.loadData(attributes, tenantId, headers, context)
      .compose(num -> setupDefaultData(DEFAULT_JOB_PROFILE_SQL, headers, context)
        .compose(r -> setupDefaultData(DEFAULT_MARC_FIELD_PROTECTION_SETTINGS_SQL, headers, context))
        .compose(d -> setupDefaultData(DEFAULT_OCLC_JOB_PROFILE_SQL, headers, context))
        .compose(u -> setupDefaultData(DEFAULT_OCLC_UPDATE_JOB_PROFILE_SQL, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_QM_INSTANCE_AND_SRS_MARC_BIB_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_QM_HOLDINGS_AND_SRS_MARC_HOLDINGS_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_INSTANCE_AND_MARC_BIB_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_EDIFACT_MAPPING_PROFILES, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_MARC_AUTHORITY_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_MARC_HOLDINGS_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(UPDATE_DEFAULT_QM_INSTANCE_AND_SRS_MARC_BIB_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_MARC_AUTHORITY_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_MARC_HOLDINGS_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_QM_SRS_MARC_HOLDINGS_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_INSTANCE_AND_MARC_BIB_CREATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_OCLC_JOB_PROFILE_SQL, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_OCLC_UPDATE_JOB_PROFILE_SQL, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_UPDATE_EDIFACT_MAPPING_PROFILES, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_DELETE_MARC_AUTHORITY_JOB_PROFILES, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_QM_AUTHORITY_UPDATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_QM_MARC_BIB_UPDATE_JOB_PROFILE, headers, context))
        .compose(m -> setupDefaultData(DEFAULT_QM_HOLDINGS_UPDATE_JOB_PROFILE, headers, context))
        .map(num));
  }

  private Future<List<String>> setupDefaultData(String script, Map<String, String> headers, Context context) {
    try {
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(script);

      if (inputStream == null) {
        LOGGER.info("setupDefaultData:: Default data was not initialized: no resources found: {}", script);
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
      LOGGER.warn("setupDefaultData:: Failed to initialize default data", e);
      return Future.failedFuture(e);
    }
  }

}
