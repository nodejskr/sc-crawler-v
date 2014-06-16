package io.vertx.shopcrawler.infomanager;

import java.sql.*;
import java.util.Properties;

import org.vertx.java.core.Handler;

public class DbConnect {
	private static DbConnect instance = null;
	private Connection conn = null;

	private DbConnect() throws SQLException {
		String url = "jdbc:postgresql://localhost/shopcrawler";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","tpqhdTl");

		conn = DriverManager.getConnection(url, props);
	}

	public static DbConnect getInstance() throws SQLException {
		if (instance == null) {
			instance = new DbConnect();
		}

		return instance;
	}

	public ResultSet excuteQuery(String query, Handler<ResultSet> handler) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);

		handler.handle(rs);

		rs.close();
		st.close();

		return rs;
	}
}