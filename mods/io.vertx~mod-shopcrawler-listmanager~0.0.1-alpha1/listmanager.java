import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;

import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;

import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

public class listmanager extends Verticle {

  static EventBus eb;

  /*******************************************
   * shop add msg handler
   ******************************************/
  private class shop_list_add implements Handler<Message>
  {
	public void handle( Message message ){
		//System.out.println("ListManager : --> Download" + message.body() );
		// 지금은 그냥 바로 downloader 로 넘길까?
		eb.send( "shop.download.parse", message.body() );
	}
  };

  public void start() {
    
	System.out.println("Start List Manager");
	
	eb = vertx.eventBus();
	eb.registerHandler("shop.list.add", new shop_list_add() );

	// publish cloadComplete msg 
	eb.publish( "shop.module.load.complete", "listmanager");

  }
}