package org.datacol.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;
import jakarta.inject.Inject;

public class API extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AbstractVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {
        // Levantar endpoints del router de los EntityController.
    }



}
