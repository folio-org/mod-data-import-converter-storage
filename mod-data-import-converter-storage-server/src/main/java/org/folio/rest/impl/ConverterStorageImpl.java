package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.folio.rest.jaxrs.resource.ConverterStorage;

import javax.ws.rs.core.Response;
import java.util.Map;

public class ConverterStorageImpl implements ConverterStorage {

  @Override
  public void getConverterStorage(Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler,
                                  Context vertxContext) {
    // TODO replace stub response
    asyncResultHandler.handle(Future.succeededFuture(
      GetConverterStorageResponse.respond500WithTextPlain("Endpoint is not implemented")));
  }
}
