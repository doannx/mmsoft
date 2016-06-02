package model;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Implementation class for the User service.
 * 
 * @author NX.Doan, @version 1.0
 * @date 20160602
 */
public class User extends AbstractVerticle {
    /**
     * Log.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Start service.
     */
    @Override
    public void start() {
        getVertx().eventBus().consumer("ping-pong", message -> {
            // call to DatabaseMock
            JsonObject jParam = (JsonObject) message.body();
            log.info("calling to DatabaseMock." + jParam.getString("action") + "...");
            getVertx().eventBus().send("ping-pong", message.body(), response -> {
                if (response.succeeded()) {
                    // response to Orchestrator
                    message.reply(response.result().body());
                } else {
                    log.info("error?");
                }
            });
        }).completionHandler(event -> {
            if (event.succeeded())
                log.info("complete handler");
            else
                log.info("failed");
        });
        log.info("User service started...");
    }
}