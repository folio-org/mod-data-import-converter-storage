package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.jaxrs.model.MatchProfile;
import org.folio.rest.jaxrs.model.MatchProfileUpdateDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.folio.rest.impl.MatchProfileTest.MATCH_PROFILES_PATH;
import static org.hamcrest.core.Is.is;

@RunWith(VertxUnitRunner.class)
public class DefaultMatchProfileTest extends AbstractRestVerticleTest{

  private static final String OCLC_INSTANCE_UUID_MATCH_PROFILE_ID = "31dbb554-0826-48ec-a0a4-3c55293d4dee";

  @Test
  public void shouldAddAndRemoveTagsDefaultProfile() {
    Tags tags = new Tags().withTagList(Arrays.asList("Lorem", "ipsum"));
    var profile = getMappingProfileById(OCLC_INSTANCE_UUID_MATCH_PROFILE_ID);
//    Add tags to default profile
    RestAssured.given()
      .spec(spec)
      .body(new MatchProfileUpdateDto().withProfile(profile.withTags(tags)))
      .when()
      .put(MATCH_PROFILES_PATH + "/" + OCLC_INSTANCE_UUID_MATCH_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(tags.getTagList()));

    profile = getMappingProfileById(OCLC_INSTANCE_UUID_MATCH_PROFILE_ID);
//    Delete tags from default profile
    RestAssured.given()
      .spec(spec)
      .body(new MatchProfileUpdateDto().withProfile(profile.withTags(new Tags().withTagList(Collections.emptyList()))))
      .when()
      .put(MATCH_PROFILES_PATH + "/" + OCLC_INSTANCE_UUID_MATCH_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(Matchers.empty()));
  }

  private MatchProfile getMappingProfileById(String id) {
    return RestAssured.given()
      .spec(spec)
      .when()
      .get(MATCH_PROFILES_PATH + "/" + id)
      .then().extract().as(MatchProfile.class);
  }
}
