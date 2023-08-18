package org.datacol.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.datacol.aux.Consts;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class API extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(API.class);
    private DeliveryOptions DOPTS = new DeliveryOptions();

    @ConfigProperty(name = "api.port")
    int PORT;

    @Inject
    EventBus bus;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        try {
            Router router = Router.router(vertx);

            router.route().handler(BodyHandler.create());
            setRoutes(router);
            vertx.createHttpServer()
                    .exceptionHandler(ex -> {
                        logger.error("Error initializing server: " + ex.getMessage());
                    })
                    .requestHandler(router)
                    .listen(PORT)
                    .onSuccess(ar -> promise.complete())
                    .onFailure(e -> {
                        logger.error("Error starting API ... " + e.getMessage());
                        promise.fail(e.getMessage());
                    });
        } catch (Exception e) {
            logger.error("API error: " + e.getMessage());
            promise.fail(e);
        }
    }

    /**
     * This method return all data about a specific range.
     * @Filters: Page Size, age, etc...
     **/
    // TODO: define more filters.
    private void getAllData(RoutingContext rc) {
        MultiMap params = rc.queryParams();
        var msg = new JsonObject()
                .put(Consts.PAGE_SIZE, params.get(Consts.PAGE_SIZE));
        bus.request("get-all", msg, DOPTS)
                .onSuccess(ar -> {
                    var response = (JsonObject) ar.body();
                    logger.info("Response: " + response);

                    reply(rc, response);
                })
                .onFailure(e -> {
                    logger.error("Get all data error: " + e.getMessage());
                    var error = new JsonObject().put("error", e.getMessage());

                    reply(rc, error);
                });
    }

    private void getDataById(RoutingContext rc) {

    }

    private void insertData(RoutingContext rc) {
        var body = rc.body().asJsonObject();
        bus.request("insert", body, DOPTS)
                .onSuccess(ar -> {
                    var response = (JsonObject) ar.body();
                    logger.info("Response: " + response);

                    reply(rc, response);
                })
                .onFailure(e -> {
                    logger.error("Insert data error: " + e.getMessage());
                    var error = new JsonObject().put("error", e.getMessage());

                    reply(rc, error);
                });
    }

    /**
     * This method allow inster a big amount of data at the same time
     * @Param: Array of data.
     * */
    private void bulkInsertData(RoutingContext rc) {
        var dataList = rc.body().asJsonArray();
        var msg = new JsonObject().put("data", dataList);
        bus.request("insert-bulk", msg, DOPTS)
                .onSuccess(ar -> {
                    var response = (JsonObject) ar.body();
                    logger.info("Response: " + response);

                    reply(rc, response);
                })
                .onFailure(e -> {
                    logger.error("Insert bulk data error: " + e.getMessage());
                    var error = new JsonObject().put("error", e.getMessage());

                    reply(rc, error);
                });
    }

    private void generateReport(RoutingContext routingContext) {
        bus.publish("generate-report", null);
    }

    private void setRoutes(Router router) {
        router.get("/report").handler(this::generateReport);
        router.get("/data").handler(this::getAllData);
        router.get("/data/:id").handler(this::getDataById);
        router.post("/data").handler(this::insertData);
        router.post("/data/bulk").handler(this::bulkInsertData);

    }

    private void reply(RoutingContext rc, JsonObject response) {
        setBasicHeaders(rc);
        rc.response().end(response.encodePrettily());
    }

    private void setBasicHeaders(RoutingContext rc) {
        rc.response().putHeader("Content-Type", "application/json");
    }

}
