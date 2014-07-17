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
	private static int index = 0;
	private Logger logger = Logger.getLogger(getClass());

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
		setMallType("cafe24", "/web/ghost_mall/naver_shop_summary.com.html");
		setMallType("makeshop", "/list/navernewopen.html?type=summary");
		setMallType("godo", "/shop/compare/compare_list.php?pKey=novelty&engine=naverep3");
		setMallType("gabia", "/naver_shop_list_sum.html");
		setMallType("mallforyou", "/mallsarang/acecart/bin/naver_list3.php?mode=abstract");
		setMallType("blueweb", "/letsmall/cpprice/naver_brain.php?opt=2");
		setMallType("intore", "/compare/naver_db_sum.html");
		setMallType("whois", "/FrontStore/__ipcs__/IPrice/navershop_summary.phtml");
		setMallType("ntsoft", "/talent/cpprice/naver_brain.php?opt=2");
		setMallType("nicecart", "/Nicecart4_new/Naverengine/new_enginedb_2.php");
		setMallType("dreamweb", "/price_link/naver_sprice_new.php");
		setMallType("wisa", "/_data/compare/naver/summary_prd.txt");
	}

	private void setMallType(String type, String postFix) {
		MallType mallType = new MallType();

		mallType.setMallType(type);
		mallType.setPostFix(postFix);

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
				assertEquals(json.size(), mallTypeList.size());
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
				String mallType = reply.body().getString("mall_type");
				String postFix = reply.body().getString("post_fix");
				assertEquals("Error : " + mallType + " - " + postFix, type.getMallType(), reply.body().getString("mall_type"));
				assertEquals("Error : " + mallType + " - " + postFix, type.getPostFix(), reply.body().getString("post_fix"));
			}
		});
	}

	@Test
	public void testLoadMallType1() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType2() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType3() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType4() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType5() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType6() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType7() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType8() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType9() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType10() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType11() {
		this.loadMallType(mallTypeList.get(index++));
	}

	@Test
	public void testLoadMallType12() {
		this.loadMallType(mallTypeList.get(index++));
	}

	public void testLoadMallTypes() {
		this.loadMallType();
	}

	public void testLoadMallList() {
		JsonObject json = new JsonObject();
		json.putString("command", "getMallList");

		loadList(json, new Handler<Message<JsonArray>>() {
			@Override
			public void handle(Message<JsonArray> reply) {
				JsonArray jsonArray = reply.body();
				if (jsonArray.size() != 0) {
					Mall mall = new Mall((JsonObject) jsonArray.get(0));
					assertEquals("mall list", mall.getClass(), Mall.class);
				}
			}
		});
	}
}