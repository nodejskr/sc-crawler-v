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

public class shopcrawler_launcher extends Verticle {
	static EventBus eb;

	public void start() {
		eb = vertx.eventBus();
		JsonObject config = container.config();

		// test parser
		/*Handler<Message> myHandler = new Handler<Message>() {
			public void handle( Message message ){
				System.out.println("I received a message " + message.body() );

				EventBus eb = vertx.eventBus();
				eb.publish("shop.download.parse", "http://192.168.0.1/");
			}
		};

		Handler<Message> shop_load_complete = new Handler<Message>() {
			public void handle( Message message ){
				System.out.println("I received a message " + message.body() );

				// initialized downloader ( test mode )
				if( "downloader".equals( message.body() ) ){
					EventBus eb = vertx.eventBus();
					eb.publish("shop.download.parse", "http://192.168.0.1/");
				}
			}
		};

		eb.registerHandler("shop.parse.parse", myHandler );
		eb.registerHandler("shop.load.complete", shop_load_complete );*/


		/////////////////////////////////////////////////////////////////
		// modules Run
		// Run downloader
		container.deployModule ("io.vertx~mod-shopcrawler-test~0.0.1-alpha1", config);
		//container.deployModule( "io.vertx~mod-shopcrawler-downloader~0.0.1-alpha1", config );
		container.deployModule ("io.vertx~mod-shopcrawler-informSaver~0.0.1-alpha1", config);
	}
}