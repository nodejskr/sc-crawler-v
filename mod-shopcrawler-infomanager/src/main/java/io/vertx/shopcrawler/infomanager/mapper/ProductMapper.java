package io.vertx.shopcrawler.infomanager.mapper;

import java.util.List;

import io.vertx.shopcrawler.infomanager.type.Product;

public interface ProductMapper {
	Product selectProduct(Number number);
	List<Product> selectProductList();
	void insertProduct(Product product);
}