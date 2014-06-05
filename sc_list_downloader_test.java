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

import java.util.*;

public class sc_list_downloader_test extends Verticle {
	static EventBus eb;

	/** 로딩 대기 타주는 클래스 **/
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

		/** Event 가 발생 할때 마다 check 해서 로드가 완료 된 것인지 확인!! **/
		public void check(){

			Iterator<String> iterator = elist.keySet().iterator();

			while( iterator.hasNext() ){
				String key = iterator.next();
				if( !elist.get( key ).booleanValue() ){
					// 하나라도 완성 안되었다면 그냥 리턴
					return;
				}
			};

			// 여기 까지 왔다면 다 성공한거니까 onStart()!!
			onStart();

		}

		public void handle( Message message ){
			elist.put( message.body().toString(), new Boolean(true) );
			check();
		}


		public void addModule( String modulename ){
			elist.put( modulename , new Boolean( false ) );
		};


		/** 상속 받아 구현해야 하는 함수 **/
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
				// send Get Msg
				eb.publish("shop.list.add", "http://famersbs.wordpress.com/feed/?test=1");
			};

		};
		water.addModule("listmanager");
		water.addModule("downloader");
		water.check();

			// add parser Listner
		Handler<Message> parse_parser = new Handler<Message>() {

			public void handle( Message message ){
				System.out.println("result\n " + message.body() );

				System.out.println("\n\n Shop Test finish");
			}
		};
		eb.registerHandler("shop.parse.parse", parse_parser );
			


	}
}