package io.vertx.shopcrawler.infomanager;

import static org.junit.Assert.*;
import io.vertx.shopcrawler.infomanager.DbConnect;
import io.vertx.shopcrawler.infomanager.checker.*;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.vertx.java.core.Handler;

/**
 * db connect test
 * 
 * @author cest-bon
 */
public class DbConnectTest {
	Logger logger = Logger.getLogger(getClass());
	public DbConnectTest() {
	}

	/**
	 * 테스트 핸들러 리턴
	 * 
	 * @param checkMessage
	 * @return
	 */
	private Handler<SqlSession> getHandler(final String query, final Object param, final Checkable checker) {
		return new Handler<SqlSession>() {
			@Override
			public void handle(SqlSession session) {
				Object target = session.selectOne(query, param);
				assertFalse("connection error", checker.compareTo(target));
			}
		};
	}

	/**
	 * 테이블 생성 유무 확인
	 * 
	 * @param mallName
	 */
	private void testExistTable(String tableName) {
		TableChecker checker = new TableChecker();
		checker.setCompareSource("crawling_info."+tableName);

		try {
			DbConnect conn = DbConnect.getInstance();
			conn.excuteQuery(getHandler(getCheckMethod("checkExistTable"), tableName, checker));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse("connection error", false);
		}
	}

	private String getCheckMethod(String method) {
		return "io.vertx.shopcrawler.infomanager.testdbconnect." + method;
	}

	@Test
	public void testExistSchema() {
		SchemaChecker checker = new SchemaChecker();
		checker.setCompareSource("crawling_info");

		try {
			DbConnect conn = DbConnect.getInstance();
			conn.excuteQuery(getHandler(getCheckMethod("checkExistSchema"), "crawling_info", checker));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse("connection error", true);
		}
	}

	@Test
	public void testExistTableMall() {
		testExistTable("mall");
	}

	@Test
	public void testExistTableMallType() {
		testExistTable("mall_type");
	}

	@Test
	public void testExistTableProduct() {
		testExistTable("product");
	}
}