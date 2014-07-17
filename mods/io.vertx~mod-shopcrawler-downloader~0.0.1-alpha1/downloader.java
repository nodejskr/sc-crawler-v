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
	private String type;

	public HttpResponseHandler setUrl( String url ){
		this.url = url;
		return this;
	}

	public HttpResponseHandler setType( String type ){
		this.type = type;
		return this;
	}
	  	
	public void handle(HttpClientResponse resp) {
		resp.bodyHandler( new HttpResponseBodyHandler().setUrl(url).setType(type) );
	}
  };

  private class HttpResponseBodyHandler implements Handler<Buffer>
  {
	private String url;
	private String type;

	public HttpResponseBodyHandler setUrl( String url ){
		this.url = url;
		return this;
	}

	public HttpResponseBodyHandler setType( String type ){
		this.type = type;
		return this;
	}
	  	
	public void handle(Buffer data) {
		downloader.eb.send("info.call.parser", url + "\n" + type + "\n" + data.toString() );
	}
  };


  /*******************************************
  * shop_download_parse
  ******************************************/
  private class shop_download_parse implements Handler<Message<JsonObject>>
  {
	public void handle( Message<JsonObject> message ){

		HttpClient client = vertx.createHttpClient();

		
		JsonObject obj = message.body();
		String url = obj.getString("db_url");
		String type = obj.getString("mall_type");
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

		HttpClientRequest request = client.get( uri + "?" + query , new HttpResponseHandler()
																		.setUrl( url )
																		.setType( type )
												);
		request.end();
	}
  };

  public void start() {
	System.out.println("Start downloader");
	eb = vertx.eventBus();
	eb.registerHandler("shop.download.parse", new shop_download_parse() );
	eb.publish( "shop.module.load.complete", "info.call.downloader");
  }
}