package io.vertx.shopcrawler.infomanager;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import io.vertx.shopcrawler.infomanager.mapper.MallMapper;
import io.vertx.shopcrawler.infomanager.mapper.MallTypeMapper;
import io.vertx.shopcrawler.infomanager.mapper.ProductMapper;
import io.vertx.shopcrawler.infomanager.type.*;

public class InfoLoader {
	private DbConnect dbConn;
	private MallType mallType;
	private List<MallType> mallTypes;
	private Product product;
	private List<Product> products;
	private Mall mall;
	private List<Mall> malls;

	public InfoLoader(DbConnect dbConn) {
		this.dbConn = dbConn;
	}

	public JsonObject getMallType(final String name) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				MallTypeMapper mallTypeMapper = session.getMapper(MallTypeMapper.class);
				mallType = mallTypeMapper.selectMallType(name);
			}
		});

		return mallType.toJson();
	}

	public JsonArray getMallType() {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				MallTypeMapper mallTypeMapper = session.getMapper(MallTypeMapper.class);
				mallTypes = mallTypeMapper.selectMallTypeList();
			}
		});

		JsonArray json = new JsonArray();
		for (MallType mallType : mallTypes) {
			json.add(mallType.toJson());
		}

		return json;
	}

	public JsonObject getProduct(final Number number) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				ProductMapper productMapper = session.getMapper(ProductMapper.class);
				product = productMapper.selectProduct(number);
			}
		});

		return product.toJson();
	}

	public JsonArray getProduct() {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				ProductMapper productMapper = session.getMapper(ProductMapper.class);
				products = productMapper.selectProductList();
			}
		});

		JsonArray json = new JsonArray();
		for (Product product : products) {
			json.add(product.toJson());
		}

		return json;
	}

	public JsonArray getMall(List<String> getters) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				MallMapper mallMapper = session.getMapper(MallMapper.class);
				malls = mallMapper.selectMallList();
			}
		});

		JsonArray jsonArray = new JsonArray();
		for (Mall mall : malls) {
			jsonArray.add(mall.toJson(getters));
		}

		return jsonArray;
	}
}