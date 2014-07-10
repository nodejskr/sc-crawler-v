package io.vertx.shopcrawler.infomanager.type;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

public class MallType implements TypeImpl {
	private String mall_type;
	private String sur_fix;

	public MallType() {
	}

	public MallType(String mall_type, String sur_fix) {
		this.setMallType(mall_type);
		this.setSurFix(sur_fix);
	}

	public MallType(JsonObject source) {
		this(source.getString("mall_type"),source.getString("sur_fix"));
	}

	public String getMallType() {
		return mall_type;
	}
	public void setMallType(String mall_type) {
		this.mall_type = mall_type;
	}
	public String getSurFix() {
		return sur_fix;
	}
	public void setSurFix(String sur_fix) {
		this.sur_fix = sur_fix;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.putString("mall_type", getMallType());
		json.putString("sur_fix", getSurFix());

		return json;
	}

	@Override
	public JsonObject toJson(List<String> getters) {
		JsonObject json = new JsonObject();

		if (getters.contains("mall_type")) {
			json.putString("mall_type", getMallType());
		}

		if (getters.contains("sur_fix")) {
			json.putString("sur_fix", getSurFix());
		}

		return json;
	}
}