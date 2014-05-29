import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

public class test extends Verticle {
	public void start() {
		EventBus eb = vertx.eventBus();

		Handler<Message> testInformSaver = new Handler<Message>() {
			public void handle( Message message ){

				// test inform saver
				if( "test".equals( message.body() ) ){
					EventBus eb = vertx.eventBus();
					eb.publish("shop.saver.product", "select");
				}
			}
		};

		eb.registerHandler("shop.test.saver", testInformSaver);
	}
}