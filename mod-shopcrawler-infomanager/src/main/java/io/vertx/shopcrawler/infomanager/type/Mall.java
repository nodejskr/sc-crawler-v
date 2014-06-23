package io.vertx.shopcrawler.infomanager.type;

import java.util.Iterator;
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
		this.idx = idx;
		this.mall_type = mall_type;
		this.mall_id = mall_id;
		this.mall_url = mall_url;
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
	public void setMallId(String post_fix) {
		this.mall_id = post_fix;
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
		json.putNumber("idx", idx);
		json.putString("mall_type", mall_type);
		json.putString("mall_id", mall_id);
		json.putString("mall_url", mall_url);

		return json;
	}

	@Override
	public JsonObject toJson(List<String> getters) {
		JsonObject json = new JsonObject();

		if (getters.contains("idx")) {
			json.putNumber("idx", idx);
		}
		if (getters.contains("mall_type")) {
			json.putString("mall_type", mall_type);
		}
		if (getters.contains("mall_id")) {
			json.putString("mall_id", mall_id);
		}
		if (getters.contains("mall_url")) {
			json.putString("mall_url", mall_url);
		}

		return json;
	}
}