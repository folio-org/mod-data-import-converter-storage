package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.JobProfile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.folio.rest.impl.JobProfileTest.JOB_PROFILES_PATH;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class DefaultJobProfileTest extends AbstractRestVerticleTest {


  private static final String DEFAULT_MARC_AUTHORITY_PROFILE_ID = "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3";
  private static final String DEFAULT_MARC_HOLDINGS_PROFILE_ID = "80898dee-449f-44dd-9c8e-37d5eb469b1d";

  @Test
  public void shouldReturnDefaultProfilesListOnGet() {
    RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("totalRecords", is(6));
  }

  @Test
  public void shouldReturnMarcAuthorityProfileOnGetById() {
    final var profile = RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + DEFAULT_MARC_AUTHORITY_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK).extract().as(JobProfile.class);
    Assert.assertEquals( "Default - Create SRS MARC Authority", profile.getName());
    Assert.assertEquals( JobProfile.DataType.MARC, profile.getDataType());
  }
  @Test

  public void shouldReturnMarcHoldingsProfileOnGetById() {
    final var profile = RestAssured.given()
      .spec(spec)
      .when()
      .get(JOB_PROFILES_PATH + "/" + DEFAULT_MARC_HOLDINGS_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK).extract().as(JobProfile.class);
    Assert.assertEquals( "Default - Create Holdings and SRS MARC Holdings", profile.getName());
    Assert.assertEquals( JobProfile.DataType.MARC, profile.getDataType());
  }
}
