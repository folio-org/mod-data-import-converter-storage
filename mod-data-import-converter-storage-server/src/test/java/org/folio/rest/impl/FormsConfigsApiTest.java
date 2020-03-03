package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.FormConfig;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(VertxUnitRunner.class)
public class FormsConfigsApiTest extends AbstractRestVerticleTest{

  public static final String FORMS_CONFIGS_PATH = "/converter-storage/forms/configs";
  public static final String FORMS_CONFIGS_TABLE = "forms_configs";

  private FormConfig formConfig = new FormConfig()
    .withFormName("matchProfilesForm")
    .withConfig(new JsonObject());

  @Test
  public void shouldReturnEmptyCollectionOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("formConfigs", empty());
  }

  @Test
  public void shouldReturnAllFormConfigsOnGet() {
    RestAssured.given()
      .spec(spec)
      .body(Json.encode(formConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("formName", is(formConfig.getFormName()))
      .body("config", notNullValue());

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(1))
      .body("formConfigs.size", is(1));
  }

  @Test
  public void shouldCreateFormConfigOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(Json.encode(formConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("formName", is(formConfig.getFormName()))
      .body("config", notNullValue());
  }

  @Test
  public void shouldReturnBadRequestOnPostWithoutFormName() {
    FormConfig invalidConfig = new FormConfig()
      .withConfig(new JsonObject());

    RestAssured.given()
      .spec(spec)
      .body(Json.encode(invalidConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnFormConfigOnGetByFormName() {
    RestAssured.given()
      .spec(spec)
      .body(Json.encode(formConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FORMS_CONFIGS_PATH + "/" + formConfig.getFormName())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("formName", is(formConfig.getFormName()))
      .body("config", notNullValue());
  }

  @Test
  public void shouldReturnNotFoundOnGetByFormNameWhenFormConfigDoesNotExist() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(FORMS_CONFIGS_PATH + "/" + formConfig.getFormName())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldUpdateFormConfigOnPutByFormName() {
    FormConfig configToUpdate = new FormConfig()
      .withFormName("matchProfilesForm")
      .withConfig(new JsonObject()
        .put("caption", "ui-data-import.summary"));

    RestAssured.given()
      .spec(spec)
      .body(Json.encode(formConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body(notNullValue());

    RestAssured.given()
      .spec(spec)
      .body(Json.encode(configToUpdate))
      .when()
      .put(FORMS_CONFIGS_PATH + "/" + configToUpdate.getFormName())
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .body("formName", is(configToUpdate.getFormName()))
      .body("config.caption", is(JsonObject.mapFrom(configToUpdate.getConfig()).getValue("caption")));
  }

  @Test
  public void shouldReturnNotFoundOnPutByFormNameWhenFormConfigDoesNotExist() {
    FormConfig configToUpdate = new FormConfig()
      .withFormName("matchProfilesForm")
      .withConfig(new JsonObject()
        .put("caption", "ui-data-import.summary"));

    RestAssured.given()
      .spec(spec)
      .body(Json.encode(configToUpdate))
      .when()
      .put(FORMS_CONFIGS_PATH + "/" + formConfig.getFormName())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldDeleteFormConfigByFormNameOnDelete() {
    RestAssured.given()
      .spec(spec)
      .body(Json.encode(formConfig))
      .when()
      .post(FORMS_CONFIGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body(notNullValue());

    RestAssured.given()
      .spec(spec)
      .when()
      .delete(FORMS_CONFIGS_PATH + "/" + formConfig.getFormName())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  public void shouldReturnNotFoundOnDeleteByFormNameWhenFormConfigDoesNotExist() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(FORMS_CONFIGS_PATH + "/" + formConfig.getFormName())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient pgClient = PostgresClient.getInstance(vertx, TENANT_ID);
    pgClient.delete(FORMS_CONFIGS_TABLE, new Criterion(), ar -> {
      if (ar.failed()) {
        context.fail(ar.cause());
      }
      async.complete();
    });
  }
}
