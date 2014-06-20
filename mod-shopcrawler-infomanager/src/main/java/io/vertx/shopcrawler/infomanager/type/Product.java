package io.vertx.shopcrawler.infomanager.type;

import org.vertx.java.core.json.JsonObject;

public class Product implements TypeImpl {
	private int idx;
	private int mall_idx;
	private int price;
	private String name;
	private String img_uri;

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getMallIdx() {
		return mall_idx;
	}
	public void setMallIdx(int mall_idx) {
		this.mall_idx = mall_idx;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUri() {
		return img_uri;
	}
	public void setImgUri(String img_uri) {
		this.img_uri = img_uri;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.putNumber("idx", idx);
		json.putNumber("mall_idx", mall_idx);
		json.putNumber("price", price);
		json.putString("name", name);
		json.putString("img_uri", img_uri);

		return json;
	}
}