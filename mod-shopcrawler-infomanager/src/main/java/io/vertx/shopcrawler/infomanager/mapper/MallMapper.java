package io.vertx.shopcrawler.infomanager.mapper;

import java.util.List;

import io.vertx.shopcrawler.infomanager.type.Mall;

public interface MallMapper {
	Mall selectMall(Number number);
	List<Mall> selectMallList();
	void insertMall(Mall mall);
}