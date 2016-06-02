package mmsofttest.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import mmsofttest.dto.Message;

/**
 * Implementation class for the Orchestrator HTTP service.
 * 
 * @author NX.Doan, @version 1.0
 * @date 20160602
 */
public class Orchestrator extends AbstractVerticle {
    /**
     * Log.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Main entry point.
     */
    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("ThisIsMyKey", "ThisIsMyKey");
            response.putHeader("content-type", "text/html").end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        // Serve static resources from the /assets directory
        router.route("/assets/*").handler(StaticHandler.create("assets"));
        router.get("/users").handler(this::getAll);
        router.post("/users/:id/messages").handler(this::addMessageById);
        router.get("/users/:id/messages").handler(this::getMessagesById);

        // start server
        vertx.createHttpServer().requestHandler(router::accept).listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });
    }

    /**
     * Get all users.
     * 
     * @param routingContext
     */
    private void getAll(RoutingContext routingContext) {
        // call to User service
        log.info("calling to User service...");
        // json parameter
        JsonObject jParam = new JsonObject();
        jParam.put("action", "getAll");
        // call
        getVertx().eventBus().send("ping-pong", jParam, response -> {
            if (response.succeeded()) {
                // submit to client
                log.info(response.result().body());
                routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response.result().body()));
            } else {
                log.info("error?");
            }
        });
    }

    /**
     * Add new message to one specific [user].
     * 
     * @param routingContext
     */
    private void addMessageById(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            //TODO: can not get parameter from JSON
            log.info(routingContext.getBodyAsJson().getValue("messageTitle"));
            final Message newMess = Json.decodeValue(routingContext.getBodyAsString(), Message.class);
            // call to User service
            log.info("calling to User service...");
            // json parameter
            JsonObject jParam = new JsonObject();
            jParam.put("action", "addMessageById");
            jParam.put("id", id);
            jParam.put("title", newMess.getMessageTitle());
            jParam.put("content", newMess.getMessageContent());
            // call
            getVertx().eventBus().send("ping-pong", jParam, response -> {
                if (response.succeeded()) {
                    // submit to client
                    routingContext.response().setStatusCode(201)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(newMess));
                } else {
                    log.info("error?");
                }
            });
        }
    }

    /**
     * Get message list of one specific [user].
     * 
     * @param routingContext
     */
    private void getMessagesById(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            // call to User service
            log.info("calling to User service...");
            // json parameter
            JsonObject jParam = new JsonObject();
            jParam.put("action", "getMessagesById");
            jParam.put("id", id);
            getVertx().eventBus().send("ping-pong", jParam, response -> {
                if (response.succeeded()) {
                    // submit to client
                    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(response.result().body()));
                } else {
                    log.info("error?");
                }
            });
        }
        routingContext.response().setStatusCode(204).end();
    }
}