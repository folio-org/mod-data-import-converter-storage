package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.MarcFieldProtectionSetting;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(VertxUnitRunner.class)
public class FieldProtectionSettingsApiTest extends AbstractRestVerticleTest {

  private static final String FIELD_PROTECTION_SETTINGS_PATH = "/field-protection-settings/marc";
  public static final String MARC_FIELD_PROTECTION_SETTINGS_TABLE = "marc_field_protection_settings";

  private static MarcFieldProtectionSetting setting_1 = new MarcFieldProtectionSetting()
    .withField("001")
    .withIndicator1("")
    .withIndicator2("")
    .withSubfield("")
    .withData("*")
    .withSource(MarcFieldProtectionSetting.Source.SYSTEM);
  private static MarcFieldProtectionSetting setting_2 = new MarcFieldProtectionSetting()
    .withField("999")
    .withIndicator1("f")
    .withIndicator2("f")
    .withSubfield("*")
    .withData("*")
    .withSource(MarcFieldProtectionSetting.Source.SYSTEM);
  private static MarcFieldProtectionSetting setting_3 = new MarcFieldProtectionSetting()
    .withField("500")
    .withIndicator1("a")
    .withIndicator2("a")
    .withSubfield("1")
    .withData("*")
    .withSource(MarcFieldProtectionSetting.Source.USER);

  @Test
  public void shouldReturnEmptyListOnGetIfThereIsNoSettings() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(0))
      .body("marcFieldProtectionSettings", empty());
  }

  @Test
  public void shouldReturnAllSettingsOnGetWhenNoQueryIsSpecified() {
    List<MarcFieldProtectionSetting> settingsToPost = Arrays.asList(setting_1, setting_2, setting_3);
    for (MarcFieldProtectionSetting setting : settingsToPost) {
      RestAssured.given()
        .spec(spec)
        .body(setting)
        .when()
        .post(FIELD_PROTECTION_SETTINGS_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(settingsToPost.size()));
  }

  @Test
  public void shouldReturnSettingsWithSourceSystem() {
    List<MarcFieldProtectionSetting> settingsToPost = Arrays.asList(setting_1, setting_2, setting_3);
    for (MarcFieldProtectionSetting setting : settingsToPost) {
      RestAssured.given()
        .spec(spec)
        .body(setting)
        .when()
        .post(FIELD_PROTECTION_SETTINGS_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH + "?query=source=" + MarcFieldProtectionSetting.Source.SYSTEM)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(2))
      .body("marcFieldProtectionSettings*.source", everyItem(is(MarcFieldProtectionSetting.Source.SYSTEM.toString())));
  }

  @Test
  public void shouldReturnLimitedCollectionOnGetWithLimit() {
    List<MarcFieldProtectionSetting> settingsToPost = Arrays.asList(setting_1, setting_2, setting_3);
    for (MarcFieldProtectionSetting setting : settingsToPost) {
      RestAssured.given()
        .spec(spec)
        .body(setting)
        .when()
        .post(FIELD_PROTECTION_SETTINGS_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
    }

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH + "?limit=2")
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("marcFieldProtectionSettings.size()", is(2))
      .body("totalRecords", is(settingsToPost.size()));
  }

  @Test
  public void shouldReturnBadRequestOnPostWhenNoSettingPassedInBody() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPostWhenInvalidFieldPassedInBody() {
    JsonObject setting = JsonObject.mapFrom(setting_1)
      .put("invalidField", "value");

    RestAssured.given()
      .spec(spec)
      .body(setting.encode())
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldCreateSettingOnPost() {
    RestAssured.given()
      .spec(spec)
      .body(setting_3)
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .body("id", notNullValue())
      .body("field", is(setting_3.getField()))
      .body("indicator1", is(setting_3.getIndicator1()))
      .body("indicator2", is(setting_3.getIndicator2()))
      .body("subfield", is(setting_3.getSubfield()))
      .body("data", is(setting_3.getData()));
  }

  @Test
  public void shouldReturnBadRequestOnPutWhenNoSettingPassedInBody() {
    RestAssured.given()
      .spec(spec)
      .body(new JsonObject().toString())
      .when()
      .put(FIELD_PROTECTION_SETTINGS_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestOnPutWhenInvalidFieldPassedInBody() {
    JsonObject invalidFileExtension = JsonObject.mapFrom(setting_1)
      .put("invalidField", "value");

    RestAssured.given()
      .spec(spec)
      .body(invalidFileExtension.encode())
      .when()
      .put(FIELD_PROTECTION_SETTINGS_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnNotFoundOnPutWhenSettingDoesNotExist() {
    RestAssured.given()
      .spec(spec)
      .body(setting_3)
      .when()
      .put(FIELD_PROTECTION_SETTINGS_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnBadRequestIfSourceSystem() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(setting_1)
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MarcFieldProtectionSetting setting = createResponse.body().as(MarcFieldProtectionSetting.class);
    setting.setIndicator1("3");

    RestAssured.given()
      .spec(spec)
      .body(setting)
      .when()
      .put(FIELD_PROTECTION_SETTINGS_PATH + "/" + setting.getId())
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Test
  public void shouldUpdateExistingSettingOnPut(TestContext context) {
    Async async = context.async();
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(setting_3)
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MarcFieldProtectionSetting setting = createResponse.body().as(MarcFieldProtectionSetting.class);
    setting.setIndicator1("1");
    async.complete();

    async = context.async();
    RestAssured.given()
      .spec(spec)
      .body(setting)
      .when()
      .put(FIELD_PROTECTION_SETTINGS_PATH + "/" + setting.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(setting.getId()))
      .body("field", is(setting.getField()))
      .body("indicator1", is(setting.getIndicator1()))
      .body("indicator2", is(setting.getIndicator2()))
      .body("subfield", is(setting.getSubfield()))
      .body("data", is(setting.getData()));
    async.complete();
  }

  @Test
  public void shouldReturnNotFoundOnGetByIdWhenSettingDoesNotExist() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldReturnExistingFileExtensionOnGetById() {
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(setting_3)
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MarcFieldProtectionSetting setting = createResponse.body().as(MarcFieldProtectionSetting.class);

    RestAssured.given()
      .spec(spec)
      .when()
      .get(FIELD_PROTECTION_SETTINGS_PATH + "/" + setting.getId())
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("id", is(setting.getId()))
      .body("field", is(setting.getField()))
      .body("indicator1", is(setting.getIndicator1()))
      .body("indicator2", is(setting.getIndicator2()))
      .body("subfield", is(setting.getSubfield()))
      .body("data", is(setting.getData()));
  }

  @Test
  public void shouldReturnNotFoundOnDeleteWhenSettingDoesNotExist() {
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(FIELD_PROTECTION_SETTINGS_PATH + "/" + UUID.randomUUID().toString())
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void shouldDeleteExistingFileExtensionOnDelete(TestContext testContext) {
    Async async = testContext.async();
    Response createResponse = RestAssured.given()
      .spec(spec)
      .body(setting_1)
      .when()
      .post(FIELD_PROTECTION_SETTINGS_PATH);
    Assert.assertThat(createResponse.statusCode(), is(HttpStatus.SC_CREATED));
    MarcFieldProtectionSetting setting = createResponse.body().as(MarcFieldProtectionSetting.class);
    async.complete();

    async = testContext.async();
    RestAssured.given()
      .spec(spec)
      .when()
      .delete(FIELD_PROTECTION_SETTINGS_PATH + "/" + setting.getId())
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
    async.complete();
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient pgClient = PostgresClient.getInstance(vertx, TENANT_ID);
    pgClient.delete(MARC_FIELD_PROTECTION_SETTINGS_TABLE, new Criterion(), ar -> {
      if (ar.failed()) {
        context.fail(ar.cause());
      }
      async.complete();
    });
  }

}

