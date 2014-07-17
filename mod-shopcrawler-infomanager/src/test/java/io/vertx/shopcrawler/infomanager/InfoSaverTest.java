package io.vertx.shopcrawler.infomanager;

import io.vertx.shopcrawler.infomanager.type.MallType;
import io.vertx.shopcrawler.infomanager.type.Product;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

import static org.vertx.testtools.VertxAssert.*;

public class InfoSaverTest extends TestVerticle {
	private final String address = "test.infomanager.saver";
	private final Logger logger = Logger.getLogger(this.getClass());
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

	@Test
	public void testSaveMallType() {
		testComplete();		// 테스트 할 때만 주석처리 할 것
		MallType mallType = new MallType("godo", "test.com");
		JsonObject json = new JsonObject();
		json.putString("command", "insert_malltype");
		json.putObject("data", mallType.toJson());

		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				logger.debug(reply);
				testComplete();
			}
		});
	}

	@Test
	public void testSaveMall() {
		testComplete();		// 테스트 할 때만 주석처리 할 것
		MallType mallType = new MallType("godo", "test.com");
		JsonObject json = new JsonObject();
		json.putString("command", "insert_malltype");
		json.putObject("data", mallType.toJson());

		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				logger.debug(reply);
				testComplete();
			}
		});
	}

	@Test
	public void testSaveProduct() {
		testComplete();		// 테스트 할 때만 주석처리 할 것
		Product product = new Product(3, 3000, "test", "testsetset", "http://kkk.com");
		JsonObject json = new JsonObject();
		json.putString("command", "addProductInfo");
		json.putObject("data", product.toJson());

		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				logger.debug(reply.body());
				testComplete();
			}
		});
	}

	@Test
	public void testSaveProducts() {
		//testComplete();		// 테스트 할 때만 주석처리 할 것
		Product product = new Product(3, 3000, "test", "testsetset", "http://kkk.com");
		JsonObject json = new JsonObject();

		JsonArray array = new JsonArray();
		array.add(product.toJson());
		product = new Product(3, 3100, "tesasdt", "testsetset", "http://kkk.com");
		array.add(product.toJson());

		json.putString("command", "addProductInfo");
		json.putArray("data", array);

		logger.debug(json);

		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				logger.debug(reply.body());
				testComplete();
			}
		});
	}
}