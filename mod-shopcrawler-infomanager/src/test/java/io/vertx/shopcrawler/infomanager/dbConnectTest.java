package io.vertx.shopcrawler.infomanager;

import static org.junit.Assert.*;
import io.vertx.shopcrawler.infomanager.DbConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.vertx.java.core.Handler;

/**
 * db connect test
 * 
 * @author cest-bon
 */
public class dbConnectTest {
	Logger logger = Logger.getLogger(getClass());
	public dbConnectTest() {
	}

	/**
	 * 테스트 핸들러 리턴
	 * 
	 * @param checkMessage
	 * @return
	 */
	private Handler<ResultSet> getHandler(final String checkMessage) {
		return new Handler<ResultSet>() {
			@Override
			public void handle(ResultSet rs) {
				try {
					while (rs.next()) {
						if (checkMessage.compareTo((String) rs.getObject(1).toString()) != 0) {
							assertFalse("different message : "+checkMessage, true);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * 테이블 생성 유무 확인
	 * 
	 * @param mallName
	 */
	private void testExistTable(String mallName) {
		try {
			DbConnect conn = DbConnect.getInstance();
			conn.excuteQuery("SELECT 'crawling_info."+mallName+"'::regclass", getHandler("crawling_info."+mallName));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse("not exist table : "+mallName, true);
		}
	}

	@Test
	public void testConn() {
		try {
			DbConnect conn = DbConnect.getInstance();
			conn.excuteQuery("SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'crawling_info'", getHandler("crawling_info"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertFalse("connection error", true);
			e.printStackTrace();
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