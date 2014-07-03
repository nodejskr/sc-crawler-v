package io.vertx.shopcrawler.infomanager.type;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

public class Mall implements TypeImpl {
	private Number idx;
	private String mall_type;
	private String mall_id;
	private String mall_url;

	public Mall() {
	}

	public Mall(Number idx, String mall_type, String mall_id, String mall_url) {
		this.setIdx(idx);
		this.setMallType(mall_type);
		this.setMallId(mall_id);
		this.setMallUrl(mall_url);
	}

	public Mall(JsonObject source) {
		this(source.getNumber("idx"), source.getString("mall_type"), source.getString("mall_id"), source.getString("mall_url"));
	}

	public Number getIdx() {
		return idx;
	}

	public void setIdx(Number idx) {
		this.idx = idx;
	}

	public String getMallType() {
		return mall_type;
	}
	public void setMallType(String mall_type) {
		this.mall_type = mall_type;
	}
	public String getMallId() {
		return mall_id;
	}
	public void setMallId(String mall_id) {
		this.mall_id = mall_id;
	}

	public String getMallUrl() {
		return mall_url;
	}

	public void setMallUrl(String mall_url) {
		this.mall_url = mall_url;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.putNumber("idx", getIdx());
		json.putString("mall_type", getMallType());
		json.putString("mall_id", getMallId());
		json.putString("mall_url", getMallUrl());

		return json;
	}

	@Override
	public JsonObject toJson(List<String> getters) {
		JsonObject json = new JsonObject();

		if (getters.contains("idx")) {
			json.putNumber("idx", getIdx());
		}
		if (getters.contains("mall_type")) {
			json.putString("mall_type", getMallType());
		}
		if (getters.contains("mall_id")) {
			json.putString("mall_id", getMallId());
		}
		if (getters.contains("mall_url")) {
			json.putString("mall_url", getMallUrl());
		}

		return json;
	}
}