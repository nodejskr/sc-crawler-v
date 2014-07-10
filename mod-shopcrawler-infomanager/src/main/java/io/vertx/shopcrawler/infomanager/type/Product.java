package io.vertx.shopcrawler.infomanager.type;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

public class Product implements TypeImpl {
	private int idx;
	private int mall_idx;
	private int price;
	private String name;
	private String img_uri;
	private String prd_uri;

	public Product() {
	}

	public Product(int mall_idx, int price, String name, String img_uri, String prd_uri) {
		this.setMallIdx(mall_idx);
		this.setPrice(price);;
		this.setName(name);;
		this.setImgUri(img_uri);;
		this.setPrdUri(prd_uri);
	}

	public Product(JsonObject source) {
		this(source.getInteger("mall_idx"), source.getInteger("price"), source.getString("name"), source.getString("img_uri"), source.getString("prd_uri"));
	}

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
	public String getPrdUri() {
		return prd_uri;
	}
	public void setPrdUri(String prd_uri) {
		this.prd_uri = prd_uri;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.putNumber("idx", getIdx());
		json.putNumber("mall_idx", getMallIdx());
		json.putNumber("price", getPrice());
		json.putString("name", getName());
		json.putString("img_uri", getImgUri());
		json.putString("prd_uri", getPrdUri());

		return json;
	}

	@Override
	public JsonObject toJson(List<String> getters) {
		JsonObject json = new JsonObject();

		if (getters.contains("idx")) {
			json.putNumber("idx", getIdx());
		}
		if (getters.contains("mall_idx")) {
			json.putNumber("mall_idx", getMallIdx());
		}
		if (getters.contains("price")) {
			json.putNumber("price", getPrice());
		}
		if (getters.contains("name")) {
			json.putString("name", getName());
		}
		if (getters.contains("img_uri")) {
			json.putString("img_uri", getImgUri());
		}
		if (getters.contains("prd_uri")) {
			json.putString("prd_uri", getPrdUri());
		}

		return json;
	}
}