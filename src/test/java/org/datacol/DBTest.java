package org.datacol;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



@QuarkusTest
public class DBTest {


    @Inject
    PgPool client;

    @Test
    public void dbConnectionTest(){

        client.getConnection()
                .onComplete(ar -> {
                    if (ar.succeeded()){
                        System.out.println("Conexion exitosa a la db");
                        client.close();
                    } else {
                        Assertions.fail("Error al conectar a la db.");
                    }
                });
    }
}
