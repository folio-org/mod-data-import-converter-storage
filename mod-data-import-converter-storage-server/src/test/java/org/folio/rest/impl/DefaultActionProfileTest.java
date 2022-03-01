package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.ActionProfileUpdateDto;
import org.folio.rest.jaxrs.model.Tags;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.folio.rest.impl.ActionProfileTest.ACTION_PROFILES_PATH;
import static org.hamcrest.core.Is.is;

@RunWith(VertxUnitRunner.class)
public class DefaultActionProfileTest extends AbstractRestVerticleTest {

  private static final String DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID = "7915c72e-c6af-4962-969d-403c7238b051";

  @Test
  public void shouldAddAndRemoveTagsDefaultProfile() {
    Tags tags = new Tags().withTagList(Arrays.asList("Lorem", "ipsum"));
    var profile = getActionProfileById(DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID);
//    Add tags to default profile
    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto().withProfile(profile.withTags(tags)))
      .when()
      .put(ACTION_PROFILES_PATH + "/" + DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(tags.getTagList()));

    profile = getActionProfileById(DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID);
//    Delete tags from default profile
    RestAssured.given()
      .spec(spec)
      .body(new ActionProfileUpdateDto().withProfile(profile.withTags(new Tags().withTagList(Collections.emptyList()))))
      .when()
      .put(ACTION_PROFILES_PATH + "/" + DEFAULT_CREATE_AUTHORITIES_ACTION_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(Matchers.empty()));
  }

  private ActionProfile getActionProfileById(String id) {
    return RestAssured.given()
      .spec(spec)
      .when()
      .get(ACTION_PROFILES_PATH + "/" + id)
      .then().extract().as(ActionProfile.class);
  }
}
