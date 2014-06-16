package io.vertx.shopcrawler.infomanager;

import java.sql.SQLException;

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InfoManager extends BusModBase implements Handler<Message<JsonObject>> {
	static EventBus eb;
	DbConnect conn;

	public void start() {
		super.start();

		try {
			conn = DbConnect.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	}

	@Override
	public void handle(Message<JsonObject> event) {
		// TODO Auto-generated method stub
	}
}