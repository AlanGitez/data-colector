package org.datacol.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnection;
import jakarta.inject.Inject;
import org.datacol.config.DBConfig;

public class UserVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(UserVerticle.class);

    private SqlConnection client;

    @Inject
    DBConfig dbConfig;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            // TODO: Definir config apropiada.
            var poolOptions = new PoolOptions();
            var pgOptions = new PgConnectOptions();

            pgOptions.setHost(dbConfig.users().host())
                    .setPort(dbConfig.users().port())
                    .setDatabase(dbConfig.users().database())
                    .setUser(dbConfig.users().user())
                    .setPassword(dbConfig.users().password());

            PgPool pool = PgPool.pool(vertx, pgOptions, poolOptions);
            pool.getConnection()
                    .onSuccess(conn -> {
                        this.client = conn;
                    })
                    .onFailure(e -> {
                        logger.error("Cannot connect with users database ... " + e.getMessage());
                        promise.fail(e.getMessage());
                    });
        } catch (Exception e) {
            logger.error("Error initializing datacol db client: " + e.getMessage());
            e.printStackTrace();
            promise.fail(e.getMessage());
        }
    }


}
