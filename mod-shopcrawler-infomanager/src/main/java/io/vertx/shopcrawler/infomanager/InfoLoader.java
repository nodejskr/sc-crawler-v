package io.vertx.shopcrawler.infomanager;
import io.vertx.shopcrawler.infomanager.type.Product;

public class InfoLoader {
	private DbConnect dbConn;

	public InfoLoader(DbConnect dbConn) {
		this.dbConn = dbConn;
	}

	public Product getProduct(int idx) {
		return null;
	}
}