package io.vertx.shopcrawler.infomanager;

import io.vertx.shopcrawler.infomanager.type.Mall;
import io.vertx.shopcrawler.infomanager.type.MallType;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

import static org.vertx.testtools.VertxAssert.*;

public class InfoLoaderTest extends TestVerticle {
	private final String address = "test.infomanager.loader";
	private List<MallType> mallTypeList = new ArrayList<MallType>();
	private EventBus eb;

	private void appReady() {
		super.start();
	}

	public void start() {
		VertxAssert.initialize(vertx);
		this.init();
		eb = vertx.eventBus();
		JsonObject config = new JsonObject();

		config.putString("address", address);

		container.deployModule(System.getProperty("vertx.modulename"), config, 1, new AsyncResultHandler<String>() {
			@Override
			public void handle(AsyncResult<String> event) {
				appReady();
			}
		});
	}

	/**
	 * 초기화
	 */
	private void init() {
		MallType mallType = new MallType();

		mallType.setMallType("cafe24");
		mallType.setSurFix("/web/ghost_mall/naver_shop_summary.com.html");

		mallTypeList.add(mallType);
	}

	/**
	 * 단일 로드
	 * 
	 * @param json
	 * @param isFail
	 * @param handler
	 */
	private void loadOne(final JsonObject json, final boolean isFail, final Handler<Message<JsonObject>> handler) {
		eb.send(address, json, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				if (isFail) {
					assertEquals("error", reply.body().getString("status"));
				} else {
					assertEquals("ok", reply.body().getString("status"));
				}
				handler.handle(reply);
				testComplete();
			}
		});
	}

	/**
	 * 다수 로드
	 * 
	 * @param json
	 * @param isFail
	 * @param handler
	 */
	private void loadList(final JsonObject json, final Handler<Message<JsonArray>> handler) {
		eb.send(address, json, new Handler<Message<JsonArray>>() {
			@Override
			public void handle(Message<JsonArray> reply) {
				handler.handle(reply);
				testComplete();
			}
		});
	}

	/**
	 * 다수 몰 타입
	 */
	private void loadMallType() {
		JsonObject json = new JsonObject();
		json.putString("command", "select_malltype");

		loadList(json, new Handler<Message<JsonArray>>() {
			@Override
			public void handle(Message<JsonArray> reply) {
				JsonArray json = reply.body();
				//assertEquals(json.size(), mallTypeList.size());
			}
		});
	}

	/**
	 * 단일 몰 타입
	 */
	private void loadMallType(final MallType type) {
		JsonObject json = new JsonObject();
		json.putString("command", "select_malltype");
		json.putString("mall_type", type.getMallType());

		loadOne(json, false, new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> reply) {
				assertEquals(type.getMallType(), reply.body().getString("mall_type"));
				assertEquals(type.getSurFix(), reply.body().getString("sur_fix"));
			}
		});
	}

	@Test
	public void testLoadMallType() {
		for (MallType type : mallTypeList) {
			this.loadMallType(type);
		}
	}

	@Test
	public void testLoadMallTypes() {
		this.loadMallType();
	}

	@Test
	public void testLoadMallList() {
		JsonObject json = new JsonObject();
		json.putString("command", "getMallList");

		loadList(json, new Handler<Message<JsonArray>>() {
			@Override
			public void handle(Message<JsonArray> reply) {
				JsonArray jsonArray = reply.body();
				Logger logger = Logger.getLogger(getClass());
				logger.debug(reply.body());
				/*if (jsonArray.size() != 0) {
					Mall mall = new Mall((JsonObject) jsonArray.get(0));
					assertEquals("mall list", mall.getClass(), Mall.class);
				}*/
			}
		});
	}
}