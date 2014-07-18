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

public class sc_crawler_first extends Verticle {
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

		JsonObject configDownloader = new JsonObject();
		JsonObject configListmanager = new JsonObject();
		JsonObject configParser = new JsonObject();
		JsonObject configInfomanager = new JsonObject();

		configDownloader.putString("address", "info.call.downloader");
		configListmanager.putString("address", "info.call.listmanager");
		configParser.putString("address", "info.call.parser");
		configInfomanager.putString("address", "info.call.infomanager");

		/////////////////////////////////////////////////////////////////
		// modules Run

		// Run downloader
		container.deployModule ("io.vertx~mod-shopcrawler-downloader~0.0.1-alpha1", configDownloader);
		container.deployModule ("io.vertx~mod-shopcrawler-listmanager~0.0.1-alpha1", configListmanager);
		container.deployModule ("io.vertx~mod-shopcrawler-parser~0.0.1-alpha1", configParser);
		container.deployModule ("com.shopcrawler~mod-infomanager~0.1.0-dev", configInfomanager);

		// Test Run!!
		System.out.println("Start Test!!!");
		ShopLoadingWaiterHandler water = new ShopLoadingWaiterHandler( this, eb ){

			public void onStart(){
				System.out.println("On Start...");
				eb.publish( "shop.start", "" );
			};

		};
		water.addModule("info.call.listmanager");
		water.addModule("info.call.downloader");
		water.addModule("info.call.parser");
		water.addModule("info.call.infomanager");
		water.check();

	}
}