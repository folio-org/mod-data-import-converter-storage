package org.folio.rest.impl.snapshot;

import io.restassured.RestAssured;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.impl.AbstractRestVerticleTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(VertxUnitRunner.class)
public class JobProfileSnapshotTest extends AbstractRestVerticleTest {

  private static final String SNAPSHOT_PATH = "/data-import-profiles/jobProfileSnapshots";

  @Test
  public void shouldReturnNotFoundOnGetById(TestContext testContext) {
    Async async = testContext.async();
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(SNAPSHOT_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
    async.complete();
  }

  @Test
  public void shouldBuildAndReturn500OnGetById(TestContext testContext) {
    Async async = testContext.async();
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .post(SNAPSHOT_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    async.complete();
  }
}
