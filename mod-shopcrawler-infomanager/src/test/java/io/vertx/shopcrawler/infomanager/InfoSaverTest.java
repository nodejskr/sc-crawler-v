package io.vertx.shopcrawler.infomanager;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

import static org.vertx.testtools.VertxAssert.*;

public class InfoSaverTest extends TestVerticle {
	private final String address = "test.redis.eval";
	//private final Logger logger = Logger.getLogger(this.getClass());
	private EventBus eb;

	private void appReady() {
		super.start();
	}

	public void start() {
		VertxAssert.initialize(vertx);
		eb = vertx.eventBus();
		JsonObject config = new JsonObject();

		config.putString("address", address);

		container.deployModule(System.getProperty("vertx.modulename"), config, 1, new AsyncResultHandler<String>() {
			@Override
			public void handle(AsyncResult<String> event) {
				appReady();
			}
		});
	}

	private void save(final JsonObject json, final boolean isFail, final Handler<Message<JsonObject>> handler) {
		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				if (isFail) {
					assertEquals("error", reply.body().getString("status"));
				} else {
					assertEquals("ok", reply.body().getString("status"));
				}

				handler.handle(reply);
			}
		});
	}

	@Test
	public void testSave() {
		JsonObject json = new JsonObject();
		json.putString("command", "save");
		json.putString("target", "product");

		testComplete();
		//		this.save(json, true, new Handler<Message<JsonObject>>() {
		//			@Override
		//			public void handle(Message<JsonObject> reply) {
		//				// TODO Auto-generated method stub
		//				//logger.debug("test save");
		//				testComplete();
		//			}
		//		});
	}

	@Test
	public void testEval2() {
		System.out.println("end");
		testComplete();
	}
}