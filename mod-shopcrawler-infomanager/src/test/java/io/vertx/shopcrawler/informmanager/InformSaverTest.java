package io.vertx.shopcrawler.informmanager;

import org.junit.Test;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

import static org.vertx.testtools.VertxAssert.*;

public class InformSaverTest extends TestVerticle {
	private final String address = "test.redis.eval";
	private EventBus eb;

	private void appReady() {
		super.start();
	}

	public void start() {
		VertxAssert.initialize(vertx);
		eb = vertx.eventBus();
		JsonObject config = new JsonObject();

		config.putString("address", address);

		System.out.println(System.getProperty("vertx.modulename"));
		container.deployModule(System.getProperty("vertx.modulename"), config, 1, new AsyncResultHandler<String>() {
			@Override
			public void handle(AsyncResult<String> event) {
				appReady();
			}
		});
	}

	@Test
	public void testEval1() {
		testComplete();
	}

	@Test
	public void testEval2() {
		System.out.println("end");
		testComplete();
	}
}