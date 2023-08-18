package org.datacol.core;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;

@ApplicationScoped
public class VerticlesDeployer {
    private static final Logger logger = LoggerFactory.getLogger(VerticlesDeployer.class);

    public void init(@Observes StartupEvent e, Vertx vertx, Instance<AbstractVerticle> verticles) {

        try {
            logger.info("Inicializando sistema...");

            for (AbstractVerticle verticle : verticles) {
                vertx.deployVerticle(verticle, ar -> {
                    if (!ar.succeeded()) {
                        logger.error("El sistema no pudo iniciar porque al menos un verticle no pudo ser deployado...\n" + ar.cause());
                        Quarkus.asyncExit();
                    }
                });
            }

        } catch (Exception exc) {
            logger.error(exc.getMessage());
        }
    }
}
