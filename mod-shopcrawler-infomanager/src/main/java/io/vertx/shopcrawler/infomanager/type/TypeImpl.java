package io.vertx.shopcrawler.infomanager.type;

import java.util.List;
import org.vertx.java.core.json.JsonObject;

public interface TypeImpl {
	JsonObject toJson();
	JsonObject toJson(List<String> getters);
}
