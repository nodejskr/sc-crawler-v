package io.vertx.shopcrawler.informmanager;

import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InformSaver extends Verticle {
	static EventBus eb;

	public void start() {
		eb = vertx.eventBus();

		// db config
		JsonObject dbConfig = container.config();
		dbConfig.putString("address", "shop.saver.db");
		dbConfig.putString("connection","PostgreSQL");
		dbConfig.putString("host","localhost");
		dbConfig.putNumber("port", 5432);
		dbConfig.putString("username", "postgres");
		dbConfig.putString("password" ,"zmfhffj");
		dbConfig.putString("database", "shopcrawler");

		/**
		 * product saver
		 */
		Handler<Message> productSaver = new Handler<Message>() {
			public void handle(Message message) {
				// insert product
				if ("insert".equals(message.body())) {
					System.out.println("saved product");

					//EventBus eb = vertx.eventBus();
					//eb.publish("shop.saver.complete.product", "");
				} else if("select".equals(message.body())) {
					vertx.eventBus().send("shop.saver.query", "select count(*) from product;", new Handler<Message<String>>() {
						@Override
						public void handle(Message<String> event) {
							System.out.println("Result of query:" + event.body());
						}
					});
				}
			}
		};

		/**
		 * mall saver
		 */
		Handler<Message> mallSaver = new Handler<Message>() {
			public void handle(Message message) {
				// insert product
				if ("insert".equals(message.body())) {
					System.out.println("saved mall");

					//EventBus eb = vertx.eventBus();
					//eb.publish("shop.saver.complete.product", "");
				}
			}
		};

		vertx.eventBus().registerHandler("shop.saver.query", new Handler<Message<String>>() {
			@Override
			public void handle(final Message<String> msg) {
				System.out.println("Received message:" + msg.body());

				// query object
				JsonObject json = new JsonObject();
				json.putString("action","raw");
				json.putString("command", msg.body());

				vertx.eventBus().send("shop.saver.db", json, new Handler<Message<JsonObject>>() {
					@Override
					public void handle(Message<JsonObject> dbMsg) {
						System.out.println("asdg");
						JsonObject result = dbMsg.body();
						//System.out.println(result.toString());
						msg.reply(result.getArray("results").<JsonArray>get(0).get(0).toString());
					}
				});
			}
		});

		// module
		container.deployModule ("io.vertx~mod-mysql-postgresql~0.3.0-SNAPSHOT", dbConfig, new Handler<AsyncResult<String>>() {
			@Override
			public void handle(AsyncResult<String> event) {
				System.out.println("Postgres module deployed:" + event.toString() + ",failed:" + event.failed() + ": " + event.cause());
			}
		});

		// regist handler
		eb.registerHandler("shop.saver.product", productSaver);
		eb.registerHandler("shop.saver.mall", mallSaver);

		// test
		eb.publish("shop.test.saver", "test");
	}
}