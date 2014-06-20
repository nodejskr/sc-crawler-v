package io.vertx.shopcrawler.infomanager;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.vertx.java.core.Handler;

public class DbConnect {
	private static DbConnect instance = null;
	private SqlSessionFactory sqlSessionFactory = null;

	private DbConnect() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		Logger.getLogger(getClass()).debug(sqlSessionFactory.getConfiguration().getDatabaseId());
	}

	public static DbConnect getInstance() throws IOException {
		if (instance == null) {
			instance = new DbConnect();
		}

		return instance;
	}

	public void excuteQuery(Handler<SqlSession> handler) {
		SqlSession session = sqlSessionFactory.openSession();

		try {
			handler.handle(session);
		} finally {
			session.close();
		}
	}
}