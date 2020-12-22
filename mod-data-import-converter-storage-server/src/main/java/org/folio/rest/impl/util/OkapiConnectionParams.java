package org.folio.rest.impl.util;

import io.vertx.core.MultiMap;
import io.vertx.core.http.impl.headers.HeadersMultiMap;

import java.util.Map;

/**
 * Wrapper class for Okapi connection params
 */
public final class OkapiConnectionParams {
  public static final String OKAPI_TENANT_HEADER = "x-okapi-tenant";
  public static final String OKAPI_TOKEN_HEADER = "x-okapi-token";
  public static final String OKAPI_URL_HEADER = "x-okapi-url";
  private static final int DEF_TIMEOUT = 2000;
  private final String okapiUrl;
  private final String tenantId;
  private final String token;
  private final Integer timeout;
  private MultiMap headers = new HeadersMultiMap();

  public OkapiConnectionParams(Map<String, String> okapiHeaders, Integer timeout) {
    this.okapiUrl = okapiHeaders.getOrDefault(OKAPI_URL_HEADER, "localhost");
    this.tenantId = okapiHeaders.getOrDefault(OKAPI_TENANT_HEADER, "");
    this.token = okapiHeaders.getOrDefault(OKAPI_TOKEN_HEADER, "dummy");
    this.timeout = timeout != null ? timeout : DEF_TIMEOUT;
    this.headers.addAll(okapiHeaders);
  }

  public OkapiConnectionParams(Map<String, String> okapiHeaders) {
    this(okapiHeaders, null);
  }

  public String getOkapiUrl() {
    return okapiUrl;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getToken() {
    return token;
  }

  public int getTimeout() {
    return timeout;
  }

  public MultiMap getHeaders() {
    return headers;
  }

  public void setHeaders(MultiMap headers) {
    this.headers = headers;
  }
}
