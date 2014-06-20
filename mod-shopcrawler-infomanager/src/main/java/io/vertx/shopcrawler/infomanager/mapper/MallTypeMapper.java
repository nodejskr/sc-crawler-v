package io.vertx.shopcrawler.infomanager.mapper;

import java.util.List;

import io.vertx.shopcrawler.infomanager.type.MallType;

public interface MallTypeMapper {
	MallType selectMallType(String name);
	List<MallType> selectMallTypeList();
	int insertMallType(MallType mallType);
}