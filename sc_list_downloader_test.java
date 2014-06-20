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

public class sc_list_downloader_test extends Verticle {
	static EventBus eb;

	public abstract class ShopLoadingWaiterHandler implements Handler<Message>
	{
		private Verticle vc;
		private EventBus eb;
		private Map<String, Boolean> elist = new TreeMap<String, Boolean>();

		public ShopLoadingWaiterHandler( Verticle vc, EventBus eb  ){
			this.vc = vc;
			this.eb = eb;

			// regist Start Event
			eb.registerHandler("shop.module.load.complete", this );
			
		};

		public void check(){

			Iterator<String> iterator = elist.keySet().iterator();

			while( iterator.hasNext() ){
				String key = iterator.next();
				if( !elist.get( key ).booleanValue() ){
					return;
				}
			};

			onStart();

		}

		public void handle( Message message ){
			elist.put( message.body().toString(), new Boolean(true) );
			check();
		}


		public void addModule( String modulename ){
			elist.put( modulename , new Boolean( false ) );
		};


		public abstract void onStart();
	}

	public void start() {
		eb = vertx.eventBus();
		JsonObject config = container.config();

		/////////////////////////////////////////////////////////////////
		// modules Run

		// Run downloader
		container.deployModule ("io.vertx~mod-shopcrawler-downloader~0.0.1-alpha1", config);
		container.deployModule ("io.vertx~mod-shopcrawler-listmanager~0.0.1-alpha1", config);

		// Test Run!!
		System.out.println("Start Test!!!");
		ShopLoadingWaiterHandler water = new ShopLoadingWaiterHandler( this, eb ){

			public void onStart(){
				System.out.println("On Start...");
				eb.publish( "shop.start", "" );
			};

		};
		water.addModule("listmanager");
		water.addModule("downloader");
		water.check();

			// add parser Listner
		Handler<Message> parse_parser = new Handler<Message>() {

			public void handle( Message message ){
				//System.out.println("result\n " + message.body() );

				String url = message.body().toString().split("\n")[0];

				System.out.println("\n\n Shop Test " + url );
			}
		};
		eb.registerHandler("shop.parse.parse", parse_parser );

	
			// add ListManager event
		Handler<Message> site_list = new Handler<Message>(){
			public void handle( Message message ){

				JsonArray reply_obj = new JsonArray();
				JsonObject obj = new JsonObject();
				obj.putString("url", "http://famersbs.wordpress.com/feed/?test=1");
				obj.putString("type", "my" );
				reply_obj.add( obj );
				JsonObject obj2 = new JsonObject();
				obj2.putString("url", "http://famersbs.wordpress.com/feed/?test=2");
				obj2.putString("type", "my" );
				reply_obj.add( obj2 );

				message.reply( reply_obj );

			}
		};

		eb.registerHandler( "shop.site.list", site_list );
	}
}