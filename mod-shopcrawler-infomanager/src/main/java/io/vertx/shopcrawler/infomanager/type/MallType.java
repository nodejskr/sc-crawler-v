package io.vertx.shopcrawler.infomanager.type;

import org.vertx.java.core.json.JsonObject;

public class MallType implements TypeImpl {
	private String mall_type;
	private String post_fix;

	public MallType() {
	}

	public MallType(String mall_type, String post_fix) {
		this.mall_type = mall_type;
		this.post_fix = post_fix;
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
		json.putString("mall_type", mall_type);
		json.putString("post_fix", post_fix);

		return json;
	}
}