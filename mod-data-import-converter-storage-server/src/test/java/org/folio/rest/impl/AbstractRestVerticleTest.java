package org.folio.rest.impl;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.apache.http.HttpStatus;
import org.folio.postgres.testing.PostgresTesterContainer;
import org.folio.rest.RestVerticle;
import org.folio.rest.client.TenantClient;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.folio.rest.jaxrs.model.TenantJob;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.tools.utils.ModuleName;
import org.folio.rest.tools.utils.NetworkUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_PATH;
import static org.folio.rest.impl.MappingProfileTest.MAPPING_PROFILES_PATH;

public abstract class AbstractRestVerticleTest {

  public static final String TENANT_ID = "diku";
  private static final String GET_USER_URL = "/users?query=id==";
  public static Vertx vertx;
  public static RequestSpecification spec;
  private static String USER_ID = UUID.randomUUID().toString();
  private static String useExternalDatabase;
  private static int PORT = NetworkUtils.nextFreePort();
  private static int MOCK_PORT = NetworkUtils.nextFreePort();
  private static String BASE_URL = "http://localhost:";
  private static String OKAPI_URL = BASE_URL + PORT;
  private static String MOCK_URL = BASE_URL + MOCK_PORT;
  @Rule
  public WireMockRule mockServer = new WireMockRule(
    WireMockConfiguration.wireMockConfig()
      .port(MOCK_PORT)
      .notifier(new Slf4jNotifier(true)));
  private JsonObject userResponse = new JsonObject()
    .put("users",
      new JsonArray().add(new JsonObject()
        .put("username", "@janedoe")
        .put("personal", new JsonObject().put("firstName", "Jane").put("lastName", "Doe"))))
    .put("totalRecords", 1);

  @BeforeClass
  public static void setUpClass(final TestContext context) throws Exception {
    Async async = context.async();
    vertx = Vertx.vertx();
    PostgresClient.closeAllClients();

    useExternalDatabase = System.getProperty(
      "org.folio.converter.storage.test.database",
      "embedded");

    switch (useExternalDatabase) {
      case "environment":
        System.out.println("Using environment settings");
        break;
      case "external":
        String postgresConfigPath = System.getProperty(
          "org.folio.converter.storage.test.config",
          "/postgres-conf-local.json");
        PostgresClient.setConfigFilePath(postgresConfigPath);
        break;
      case "embedded":
        PostgresClient.setPostgresTester(new PostgresTesterContainer());
        break;
      default:
        String message = "No understood database choice made." +
          "Please set org.folio.converter.storage.test.database" +
          "to 'external', 'environment' or 'embedded'";
        throw new Exception(message);
    }

    TenantClient tenantClient = new TenantClient(OKAPI_URL, TENANT_ID, "dummy-token");
    DeploymentOptions restVerticleDeploymentOptions = new DeploymentOptions()
      .setConfig(new JsonObject().put("http.port", PORT));
    vertx.deployVerticle(RestVerticle.class.getName(), restVerticleDeploymentOptions, res -> {
      try {
        TenantAttributes tenantAttributes = new TenantAttributes();
        tenantAttributes.setModuleTo(ModuleName.getModuleName() + "-1.0.0");
        tenantClient.postTenant(tenantAttributes, res2 -> {
          if (res2.result().statusCode() == 204) {
            return;
          }
          if (res2.result().statusCode() == 201) {
            tenantClient.getTenantByOperationId(res2.result().bodyAsJson(TenantJob.class).getId(), 60000, context.asyncAssertSuccess(res3 -> {
              context.assertTrue(res3.bodyAsJson(TenantJob.class).getComplete());
              String error = res3.bodyAsJson(TenantJob.class).getError();
              if (error != null) {
                context.assertEquals("Failed to make post tenant. Received status code 400", error);
              }
            }));
          } else {
            context.assertEquals("Failed to make post tenant. Received status code 400", res2.result().bodyAsString());
          }
          async.complete();
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    spec = new RequestSpecBuilder()
      .setContentType(ContentType.JSON)
      .setBaseUri(OKAPI_URL)
      .addHeader(RestVerticle.OKAPI_HEADER_TENANT, TENANT_ID)
      .addHeader(RestVerticle.OKAPI_USERID_HEADER, USER_ID)
      .addHeader(RestVerticle.OKAPI_HEADER_PREFIX + "-url", MOCK_URL)
      .build();
  }

  @AfterClass
  public static void tearDownClass(final TestContext context) {
    Async async = context.async();
    vertx.close(context.asyncAssertSuccess(res -> {
      if (useExternalDatabase.equals("embedded")) {
        PostgresClient.stopPostgresTester();
      }
      async.complete();
    }));
  }

  @Before
  public void setUp(TestContext testContext) {
    WireMock.stubFor(WireMock.get(GET_USER_URL + USER_ID)
      .willReturn(WireMock.okJson(userResponse.toString())));
  }

  @Before
  public void clearTables(TestContext context){}

  protected MappingProfileUpdateDto postMappingProfile(MappingProfileUpdateDto mappingProfileUpdateDto) {
    return postProfile(mappingProfileUpdateDto, MAPPING_PROFILES_PATH, MappingProfileUpdateDto.class);
  }

  protected ActionProfileUpdateDto postActionProfile(ActionProfileUpdateDto actionProfileUpdateDto) {
    return postProfile(actionProfileUpdateDto, ACTION_PROFILES_PATH, ActionProfileUpdateDto.class);
  }

  protected <T> T postProfile(T profile, String path, Class<T> clazz) {
    return RestAssured.given()
      .spec(spec)
      .body(profile)
      .when()
      .post(path)
      .body()
      .as(clazz);
  }

}
