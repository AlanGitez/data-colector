package org.datacol.core;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.*;
import io.vertx.core.file.FileSystem;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.inject.Inject;
import org.datacol.aux.Consts;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;
import java.util.concurrent.CompletionStage;

public class ReportVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ReportVerticle.class);

    @ConfigProperty(name = "folder.paths.out")
    String outputFolder;

    @Inject
    EventBus bus;

    WorkerExecutor reportPool;
    FileSystem fs;

    @Override
    public void start(Promise promise) throws Exception {

        try {
            var outputPath = Path.of(outputFolder);
            outputPath.toFile().mkdirs();

            this.reportPool = vertx.createSharedWorkerExecutor("reportPool", 1);
            fs = vertx.fileSystem();

            promise.complete();
        } catch (Exception e) {
            promise.fail(e.getMessage());
        }

    }

    @ConsumeEvent("generate-report")
    public CompletionStage<JsonObject> generateReportBlocking(JsonObject msg) {
        logger.info("Initializing report ... ");
        this.reportPool.executeBlocking(promise -> generateReport(msg));
        return null;
    }

    /**
     * This method generate a .CSV file with de database data.
     * @param msg: configuration json for the report data query.
     * */
    private void generateReport(JsonObject msg) {
        var reportConfig = new JsonObject();

        reportConfig.put(Consts.PAGE_SIZE, 100);
        bus.request("get-all", reportConfig)
                .onSuccess(ar -> {
                    var result = (JsonObject) ar.body();
                    logger.info("Report payload: " + result);

                    writeCSV(result);
                })
                .onFailure(e -> logger.error("Error getting data information ... " + e.getMessage()));
    }

    private void writeCSV(JsonObject json) {
        var payload = json.getJsonArray("payload");
        JsonObject firstValue = (JsonObject) payload.getValue(0);
        var path = String.format("%s/report.csv", outputFolder);
        var csvContent = new StringBuilder();

        try {
            if (!fs.existsBlocking(path)) {
                fs.createFile(path);
            }

            firstValue.forEach(entry -> csvContent.append(entry.getKey()).append(";"));
            csvContent.append("\n");
            for (Object element : payload) {
                var obj = (JsonObject) element;
                obj.forEach(entry -> csvContent.append(entry.getValue()).append(";"));
                csvContent.append("\n");
            }
            fs.writeFileBlocking(path, Buffer.buffer(csvContent.toString()));
        } catch (Exception e) {
            logger.error("Error writing report ... " + e.getMessage());
            e.printStackTrace();
        }

    }


}
