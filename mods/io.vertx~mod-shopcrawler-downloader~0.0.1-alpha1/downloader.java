import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;

import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;

import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

public class downloader extends Verticle {

  static EventBus eb;

  public void start() {

	eb = vertx.eventBus();

	Handler<Message> myHandler = new Handler<Message>() {

		public void handle( Message message ){

			System.out.println("I received a message " + message.body() );

			HttpClient client = vertx.createHttpClient()
								.setPort(80)
								.setHost("192.168.0.53")
								.setMaxPoolSize(10);

			HttpClientRequest request = client.get("/", new Handler<HttpClientResponse>() {
				public void handle(HttpClientResponse resp) {
					resp.bodyHandler(new Handler<Buffer>() {
						public void handle(Buffer data) {

							//downloader.eb.publish("shop.download.parse.complete", message.body() );
							downloader.eb.publish("shop.parse.parse", data.toString() );
						}
					});

				}
			});
			request.end();
		}
	};

	eb.registerHandler("shop.download.parse", myHandler );
	eb.publish("shop.load.complete", "downloader");

  }
}