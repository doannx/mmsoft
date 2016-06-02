package mmsofttest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import mmsofttest.http.Orchestrator;

@RunWith(VertxUnitRunner.class)
public class OrchestratorTest {
    private Vertx vertx;
    private int port;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();

        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));
        vertx.deployVerticle(Orchestrator.class.getName(), options, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMyApplication(TestContext context) {
        
    }
}
