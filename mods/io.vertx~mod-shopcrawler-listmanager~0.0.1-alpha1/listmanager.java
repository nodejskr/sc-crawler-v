import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;

import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;

import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.JsonArray;

import java.util.*;

public class listmanager extends Verticle {

	static EventBus eb;

	private String DB_REQUEST_ID = "info.call.infomanager";

	private void loadMallList(){
		// Request Data --> after write test code then move to timer
		JsonObject req_obj = new JsonObject();
		req_obj.putString( "command", "getMallList" );
		eb.send( DB_REQUEST_ID, req_obj , new Handler<Message<JsonArray>>(){
			public void handle( Message<JsonArray> message ){

				//JsonObject obj = message
				JsonArray list = message.body();
				System.out.print(list.size());

				for( int i = 0 ; i < list.size() ; ++ i ){

					JsonObject e = list.get( i );

					System.out.println("recv site_list " + i + " " + e.getString("db_url") );

					eb.send( "shop.download.parse", e );

				}
			}
		} );
	}

	private void refreshSiteList(){
		loadMallList();
	}

	public void start() {

		System.out.println("Start List Manager");

		eb = vertx.eventBus();

		// wait start signal
		eb.registerHandler("shop.start", new Handler<Message>(){
			public void handle( Message message ){
				refreshSiteList();
			}
		});

		// publish cloadComplete msg
		eb.publish( "shop.module.load.complete", "info.call.listmanager");

	}
}