package org.folio.rest.impl.association.wrapper;

import org.folio.rest.jaxrs.model.Metadata;
import org.folio.rest.jaxrs.model.UserInfo;

public interface ProfileWrapper<T> {

  String getId();

  String getName();

  UserInfo getUserInfo();

  Metadata getMetadata();

  T getProfile();

  void setProfile(T profile);

  Class<T> getProfileType();
}
