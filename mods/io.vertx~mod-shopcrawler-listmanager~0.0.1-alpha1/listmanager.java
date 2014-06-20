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

public class listmanager extends Verticle {

  static EventBus eb;

  private static void refreshSiteList(){

  		// Request Data --> after write test code then move to timer
	eb.send( "shop.site.list", "" , new Handler<Message<JsonArray>>(){
		public void handle( Message<JsonArray> message ){

			//JsonObject obj = message
			JsonArray list = message.body();

			for( int i = 0 ; i < list.size() ; ++ i ){

				JsonObject e = list.get( i );

				System.out.println("recv site_list " + i + " " + e.getString("url") );

				eb.send( "shop.download.parse", e.getString("url")  );

			}

			

		}
	} );

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
	eb.publish( "shop.module.load.complete", "listmanager");

  }
}