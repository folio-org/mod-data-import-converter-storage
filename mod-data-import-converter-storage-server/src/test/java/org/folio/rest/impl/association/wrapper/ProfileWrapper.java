package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;
import org.folio.rest.jaxrs.model.ActionProfile;
import org.folio.rest.jaxrs.model.JobProfile;
import org.folio.rest.jaxrs.model.MappingProfile;
import org.folio.rest.jaxrs.model.MatchProfile;

/**
 * Wrapper for Profile entity. It used only for tests.
 *
 * @param <T>  profile entity type such as {@link ActionProfile}, {@link JobProfile},
 *             {@link MappingProfile}, {@link MatchProfile}.
 */
public interface ProfileWrapper<T> {

  String getId();

  String getName();

  UserInfo getUserInfo();

  Metadata getMetadata();

  T getProfile();

  void setProfile(T profile);

  Class<T> getProfileType();
}
