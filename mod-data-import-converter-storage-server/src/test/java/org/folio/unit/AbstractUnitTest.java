package org.folio.unit;

import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.folio.postgres.testing.PostgresTesterContainer;
import org.folio.rest.RestVerticle;
import org.folio.rest.client.TenantClient;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.folio.rest.jaxrs.model.TenantJob;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.tools.utils.ModuleName;
import org.folio.rest.tools.utils.NetworkUtils;
import org.folio.spring.SpringContextUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public abstract class AbstractUnitTest {

  protected static final String TENANT_ID = "diku";
  protected static final String HTTP_PORT = "http.port";
  protected static final String TOKEN = "token";
  protected static Vertx vertx = Vertx.vertx();

  public AbstractUnitTest() {
    Context vertxContext = vertx.getOrCreateContext();
    SpringContextUtil.init(vertx, vertxContext, ApplicationTestConfig.class);
    SpringContextUtil.autowireDependencies(this, vertxContext);
  }

  @BeforeClass
  public static void beforeClass(TestContext context) throws Exception {
    Async async = context.async();
    int port = NetworkUtils.nextFreePort();
    String okapiUrl = "http://localhost:" + port;
    PostgresClient.closeAllClients();
    PostgresClient.setPostgresTester(new PostgresTesterContainer());
    TenantClient tenantClient = new TenantClient(okapiUrl, TENANT_ID, TOKEN);

    final DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put(HTTP_PORT, port));
    vertx.deployVerticle(RestVerticle.class.getName(), options, res -> {
      try {
        TenantAttributes tenantAttributes = new TenantAttributes();
        tenantAttributes.setModuleTo(ModuleName.getModuleName());
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
        throw new RuntimeException(e);
      }
    });
  }

  @After
  public abstract void afterTest(TestContext context);
}
