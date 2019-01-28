package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ConverterStorageTest extends AbstractRestVerticleTest {

  private static final String CONVERTER_STORAGE_PATH = "/converter-storage";

  // TODO replace stub test
  @Test
  public void shouldReturnErrorOnGETConverterStorage() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(CONVERTER_STORAGE_PATH)
      .then()
      .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
  }

}
