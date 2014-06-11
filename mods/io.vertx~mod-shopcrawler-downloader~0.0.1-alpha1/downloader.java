import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;

import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;

import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

import java.net.*;
import java.io.*;

public class downloader extends Verticle {

  static EventBus eb;

  /*******************************************
   * http get handler
   ******************************************/
   private class HttpResponseHandler implements Handler<HttpClientResponse>
  {
	private String url;

	public HttpResponseHandler( String url ){
		this.url = url;
	}
	  	
	public void handle(HttpClientResponse resp) {
		resp.bodyHandler( new HttpResponseBodyHandler( url ) );
	}
  };

  private class HttpResponseBodyHandler implements Handler<Buffer>
  {
	private String url;

	public HttpResponseBodyHandler( String url ){
		this.url = url;
	}
	  	
	public void handle(Buffer data) {
		downloader.eb.send("shop.parse.parse", url + "\n" + data.toString() );
	}
  };


  /*******************************************
  * shop_download_parse
  ******************************************/
  private class shop_download_parse implements Handler<Message>
  {
	public void handle( Message message ){
		System.out.println("req download parsemode " + message.body() );

		HttpClient client = vertx.createHttpClient();

		
		String url = message.body().toString();
		int port = 80;
		String host = "127.0.0.1";
		String uri = "";
		String query = "";
		
		try{
			URL aURL = new URL( url );
			if( -1 != aURL.getPort() ){
				port = aURL.getPort();
			}else{
				if( "http".equals( aURL.getProtocol().toLowerCase() ) ) port = 80;
				else													port = 443;
			}
			if( null == aURL.getQuery() ){
				query = "";
			}else{
				query = aURL.getQuery();
			}
			host = aURL.getHost();
			uri = aURL.getPath();
		}catch(Exception ex ){
			System.out.println( "" + ex );
		};

		System.out.println("url " +  uri + "?" + query );

		client.setPort(port)
			  .setHost(host);

		HttpClientRequest request = client.get( uri + "?" + query , new HttpResponseHandler( message.body().toString() ) );
		request.end();
	}
  };

  public void start() {
	System.out.println("Start downloader");
	eb = vertx.eventBus();
	eb.registerHandler("shop.download.parse", new shop_download_parse() );
	eb.publish( "shop.module.load.complete", "downloader");
  }
}