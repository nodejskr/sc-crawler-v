package io.vertx.shopcrawler.infomanager;

import io.vertx.shopcrawler.infomanager.type.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InfoManager extends BusModBase implements Handler<Message<JsonObject>> {
	private String baseAddress = null;
	DbConnect conn;

	public void start() {
		super.start();

		try {
			conn = DbConnect.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonObject config = container.config();
		baseAddress = config.getString("address", "io.vertx.shopcrawler.infomanager");

		eb.registerHandler(baseAddress, this);

		eb.publish( "shop.module.load.complete", "info.call.infomanager");
	}

	@Override
	public void handle(final Message<JsonObject> message) {
		String command = message.body().getString("command");

		if (command == null) {
			//sendError(message, "command must be specified");
			return;
		}

		InfoLoader loader = new InfoLoader(conn);
		InfoSaver saver = new InfoSaver(conn);

		switch (command) {
		case "addProductInfo" :
			System.out.println("chekc 1 : " + message.body().isArray());
//			if (message.body().isArray()) {
				
				JsonArray products = message.body().getArray("data");
				saver.setProduct(products);
				sendOK(message);
//			} else if (message.body().isObject()) {
//				Product product = new Product(message.body().getObject("data"));
//				saver.setProduct(product);
//				sendOK(message);
//			}
			break;
		case "getMallList" :
			List<String> getters = new ArrayList<String>();
			getters.add("mall_type");
			getters.add("db_url");
			message.reply(loader.getMall(getters));
			break;
		case "insert_product" :
			break;
		case "insert_mall" :
			break;
		case "insert_malltype" :
			MallType mallType = new MallType(message.body().getObject("data"));
			saver.setMallType(mallType);
			sendOK(message);
			break;
		case "select_product" :
			if (message.body().getString("idx") == null) {
				JsonArray reply = loader.getProduct();
				message.reply(reply);
			} else {
				JsonObject reply = loader.getProduct(message.body().getNumber("idx"));
				sendOK(message, reply);
			}
			break;
		case "select_mall" :
			break;
		case "select_malltype" :
			if (message.body().getString("mall_type") == null) {
				JsonArray reply = loader.getMallType();
				message.reply(reply);
			} else {
				JsonObject reply = loader.getMallType(message.body().getString("mall_type"));
				sendOK(message, reply);
			}
			break;
		default :
			break;
		}
	}
}