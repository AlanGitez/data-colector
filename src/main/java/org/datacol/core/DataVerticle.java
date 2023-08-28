package org.datacol.core;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.datacol.aux.Consts;
import org.datacol.aux.Translate;
import org.datacol.config.DatacolDBConfig;

import java.util.concurrent.CompletionStage;


@ApplicationScoped
public class DataVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(DataVerticle.class);

    @Inject
    DatacolDBConfig dataConnection;

    private SqlConnection client;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            // TODO: Definir config apropiada.
            var poolOptions = new PoolOptions();
            var pgOptions = new PgConnectOptions();

            pgOptions.setHost(dataConnection.host())
                    .setPort(dataConnection.port())
                    .setDatabase(dataConnection.database())
                    .setUser(dataConnection.user())
                    .setPassword(dataConnection.password());

            PgPool client = PgPool.pool(vertx, pgOptions, poolOptions);
            client.getConnection()
                    .onSuccess(ar -> {
                        this.client = ar;
                        promise.complete();
                    })
                    .onFailure(e -> promise.fail("Error connecting pg client cause " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error initializing db client: " + e.getMessage());
            e.printStackTrace();
            promise.fail(e.getMessage());
        }
    }

    @ConsumeEvent("get-all")
    public CompletionStage<JsonObject> getAllData(JsonObject msg) {
        Promise<JsonObject> promise = Promise.promise();
        try {
            var pageSize = msg.getString(Consts.PAGE_SIZE);

            PreparedQuery<RowSet<Row>> query = client.preparedQuery("SELECT * FROM data LIMIT $1");
            query.execute(Tuple.of(Integer.valueOf(pageSize)))
                    .onSuccess(rowset -> {
                        var dataList = Translate.translateAll(rowset);
                        var json = new JsonObject()
                                .put("payload", dataList);

                        promise.complete(json);
                    })
                    .onFailure(e -> promise.fail(e.getMessage()));
        } catch (Exception e) {
            promise.fail(e.getMessage());
        }

        return promise.future().toCompletionStage();
    }

    @ConsumeEvent("get-single")
    public CompletionStage<JsonObject> getSingleData(JsonObject msg) {
        Promise<JsonObject> promise = Promise.promise();

        return promise.future().toCompletionStage();
    }

    @ConsumeEvent("insert")
    public CompletionStage<JsonObject> insertData(JsonObject msg) {
        logger.info("Executing request for body: " + msg);
        Promise<JsonObject> promise  = Promise.promise();

        try {
            var name = msg.getString("name");
            var province = msg.getString("province");
            var phone = msg.getString("phone");
            var email = msg.getString("email");
            var message = msg.getString("message");
            var dni = msg.getLong("dni");

            PreparedQuery<RowSet<Row>> query = client.preparedQuery("INSERT INTO dataType_A.data (name, province, phone, email, message, dni) VALUES ($1, $2, $3, $4, $5, $6)");
            query.execute(Tuple.of(name, province, phone, email, message, dni))
                    .onSuccess(rowset -> promise.complete(msg))
                    .onFailure(e -> promise.fail(e.getMessage()));
        } catch (Exception e) {
            promise.fail(e.getMessage());
        }

        return promise.future().toCompletionStage();
    }

    @ConsumeEvent("insert-bulk")
    public CompletionStage<JsonObject> insertBulkData(JsonObject msg) {
        logger.info("Executing request for body: " + msg);
        Promise<JsonObject> promise = Promise.promise();
        var data = msg.getJsonArray("data");

        try {
            for (Object singleData : data) {
                var single = (JsonObject) singleData;
                var name = single.getString("name");
                var province = single.getString("province");
                var phone = single.getString("phone");
                var email = single.getString("email");
                var message = single.getString("message");
                var dni = single.getLong("dni");

                PreparedQuery<RowSet<Row>> query = client.preparedQuery("INSERT INTO data (name, province, phone, email, message, dni) VALUES ($1, $2, $3, $4, $5, $6)");
                query.execute(Tuple.of(name, province, phone, email, message, dni))
                        .onSuccess(rowset -> promise.complete(msg))
                        .onFailure(e -> promise.fail(e.getMessage()));
            }
        } catch (Exception e) {
            promise.fail(e.getMessage());
        }

        return promise.future().toCompletionStage();
    }
}
