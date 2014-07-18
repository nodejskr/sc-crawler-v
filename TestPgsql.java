import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestPgsql {
	/**
	 * pgsql 원격테스트입니다.
	 * 맥용 pgsql GUI클라이언트는 http://www.postgresql.org/ftp/pgadmin3/release/v1.18.1/osx/ 여기서 받으세요.
	 * 
	 * host : 115.71.238.69
	 * port : 5432
	 * database : shopcrawler
	 * user : crawler
	 * db : 0000
	 * schema : crawling_info 
	 * 
	 * PGSQL은 다른 RDBMS와는 다르게 DB > SCHEMA >TABLE의 구조로 되어있습니다.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://115.71.238.69:5432/shopcrawler","crawler", "0000");
			Statement stmt = connection.createStatement();
			
			/*
			 * PGSQL은 다른 RDBMS와는 다르게 DB > SCHEMA >TABLE의 구조로 되어있습니다.
			 * mall테이블 앞에 붙은 crawling_info는 DB명이 아닌 스키마명입니다.
			 */
			String sql = "select * from crawling_info.mall";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int idx = rs.getInt("idx");
				String malllid = rs.getString("mall_id");
				String malltype = rs.getString("mall_type");
				System.out.println(idx + malllid + malltype);
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
