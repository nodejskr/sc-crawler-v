import java.util.LinkedList;
import java.util.List;

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

public class ParserMain extends BusModBase implements
		Handler<Message<JsonObject>> {
	private String baseAddress = null;

	public void start() {
		super.start();
		JsonObject config = container.config();
		
		baseAddress = config.getString("address", "shop.parse.parse");
		eb.registerHandler(baseAddress, this);
	}

	@Override
	public void handle(Message<JsonObject> message) {
		String[] data = message.body().toString().split("\n");
		String url = data[0], type = data[1], body = this.getBody(data);
		ParserCore parser = null;
		switch (type) {
		case "cafe24":
//			parser = new ParserCafe24DBurl(url, "euc-kr");
			parser = new ParserCafe24DBurl();
			break;
		default:
			break;
		}
		// info.call
		JsonObject jsonData = new JsonObject();
		jsonData.putString("command", "addProductInfo");
		jsonData.putArray("data", parser.parse(body));
		eb.publish("shop.saver.product.save", jsonData);
		
		sendOK(message);
		message.reply();
	}
	
	private String getBody(String[] message)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 2; i < message.length; i++) {
			builder.append(message[i]);
		}
		return builder.toString();
	}

}
