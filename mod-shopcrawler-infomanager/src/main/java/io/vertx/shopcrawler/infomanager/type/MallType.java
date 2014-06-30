package io.vertx.shopcrawler.infomanager.type;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

public class MallType implements TypeImpl {
	private String mall_type;
	private String post_fix;

	public MallType() {
	}

	public MallType(String mall_type, String post_fix) {
		this.setMallType(mall_type);
		this.setPostFix(post_fix);
	}

	public MallType(JsonObject source) {
		this(source.getString("mall_type"),source.getString("post_fix"));
	}

	public String getMallType() {
		return mall_type;
	}
	public void setMallType(String mall_type) {
		this.mall_type = mall_type;
	}
	public String getPostFix() {
		return post_fix;
	}
	public void setPostFix(String post_fix) {
		this.post_fix = post_fix;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.putString("mall_type", getMallType());
		json.putString("post_fix", getPostFix());

		return json;
	}

	@Override
	public JsonObject toJson(List<String> getters) {
		JsonObject json = new JsonObject();

		if (getters.contains("mall_type")) {
			json.putString("mall_type", getMallType());
		}

		if (getters.contains("post_fix")) {
			json.putString("post_fix", getPostFix());
		}

		return json;
	}
}