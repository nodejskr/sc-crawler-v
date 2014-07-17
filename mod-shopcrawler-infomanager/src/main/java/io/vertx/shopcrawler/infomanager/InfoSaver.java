package io.vertx.shopcrawler.infomanager;

import io.vertx.shopcrawler.infomanager.mapper.MallTypeMapper;
import io.vertx.shopcrawler.infomanager.mapper.ProductMapper;
import io.vertx.shopcrawler.infomanager.type.MallType;
import io.vertx.shopcrawler.infomanager.type.Product;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InfoSaver {
	private DbConnect dbConn;

	public InfoSaver(DbConnect dbConn) {
		this.dbConn = dbConn;
	}

	public void setMallType(final String type, final String postFix) {
		MallType mallType = new MallType();
		mallType.setMallType(type);
		mallType.setPostFix(postFix);
		this.setMallType(mallType);
	}

	public void setMallType(final MallType mallType) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				MallTypeMapper mallTypeMapper = session.getMapper(MallTypeMapper.class);
				mallTypeMapper.insertMallType(mallType);
			}
		});
	}

	public void setProduct(final JsonArray products) {
		int[] a = new int[]{1,3};
		for (int i : a) {
			System.out.print(i);
		}

		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				ProductMapper productMapper = session.getMapper(ProductMapper.class);
				JsonObject json = null;
				Product product = null;

				for (int i = 0; i < products.size(); i++) {
					json = (JsonObject) products.get(i);
					product = new Product(json);
					productMapper.insertProduct(product);
				}

				session.commit();
			}
		}, ExecutorType.BATCH);
	}

	public void setProduct(final Product product) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				ProductMapper productMapper = session.getMapper(ProductMapper.class);
				productMapper.insertProduct(product);
			}
		});
	}
}