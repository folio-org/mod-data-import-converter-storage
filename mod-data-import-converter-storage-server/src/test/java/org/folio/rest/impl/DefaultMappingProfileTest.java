package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.Tags;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MappingProfileUpdateDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.folio.rest.impl.MappingProfileTest.MAPPING_PROFILES_PATH;
import static org.hamcrest.core.Is.is;

@RunWith(VertxUnitRunner.class)
public class DefaultMappingProfileTest extends AbstractRestVerticleTest  {

  private static final String DEFAULT_CREATE_AUTHORITIES_MAPPING_PROFILE_ID = "6a0ec1de-68eb-4833-bdbf-0741db25c314";
  @Test
  public void shouldAddAndRemoveTagsDefaultProfile() {
    Tags tags = new Tags().withTagList(Arrays.asList("Lorem", "ipsum"));
    var profile = getMappingProfileById(DEFAULT_CREATE_AUTHORITIES_MAPPING_PROFILE_ID);
//    Add tags to default profile
    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(profile.withTags(tags)))
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + DEFAULT_CREATE_AUTHORITIES_MAPPING_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(tags.getTagList()));

    profile = getMappingProfileById(DEFAULT_CREATE_AUTHORITIES_MAPPING_PROFILE_ID);
//    Delete tags from default profile
    RestAssured.given()
      .spec(spec)
      .body(new MappingProfileUpdateDto().withProfile(profile.withTags(new Tags().withTagList(Collections.emptyList()))))
      .when()
      .put(MAPPING_PROFILES_PATH + "/" + DEFAULT_CREATE_AUTHORITIES_MAPPING_PROFILE_ID)
      .then()
      .statusCode(HttpStatus.SC_OK)
      .body("tags.tagList", is(Matchers.empty()));
  }

  private MappingProfile getMappingProfileById(String id) {
    return RestAssured.given()
      .spec(spec)
      .when()
      .get(MAPPING_PROFILES_PATH + "/" + id)
      .then().extract().as(MappingProfile.class);
  }
}
