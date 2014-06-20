package io.vertx.shopcrawler.infomanager;

import io.vertx.shopcrawler.infomanager.mapper.MallTypeMapper;
import io.vertx.shopcrawler.infomanager.mapper.ProductMapper;
import io.vertx.shopcrawler.infomanager.type.MallType;
import io.vertx.shopcrawler.infomanager.type.Product;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
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

	public void setProduct(final Number number) {
		dbConn.excuteQuery(new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				ProductMapper productMapper = session.getMapper(ProductMapper.class);
				Product product = productMapper.selectProduct(number);
			}
		});
	}
}