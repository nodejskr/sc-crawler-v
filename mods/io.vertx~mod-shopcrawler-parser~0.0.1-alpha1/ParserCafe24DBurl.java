import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class ParserCafe24DBurl extends ParserCore {
	private String url;
	private String encoding = "euc-kr";
	private String dburl = "/web/ghost_mall/new_naver_shop.com.txt";
	private int connectionTimeout = 30;

	public ParserCafe24DBurl() {

	}

	public ParserCafe24DBurl(String baseUrl, String encoding) {
		this.url = baseUrl;
		this.encoding = encoding;
	}

	public JsonArray parse(String data) {
		BufferedReader reader = new BufferedReader(new StringReader(data));
		try {
			return this.getList(reader);
		} catch (IOException e) {
			return null;
		}
	}

	public JsonArray parse() {
		return this.getContent();
	}

	private JsonArray getContent() {
		BufferedReader br = null;
		JsonArray list = null;
		try {
			URL checkUrl = new URL(url + dburl);
			HttpURLConnection huc = (HttpURLConnection) checkUrl
					.openConnection();
			huc.setConnectTimeout(connectionTimeout);
			huc.setReadTimeout(connectionTimeout);
			huc.setRequestMethod("GET");
			huc.connect();
			br = new BufferedReader(new InputStreamReader(huc.getInputStream(),
					encoding));
			list = getList(br);
			huc.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private JsonArray getList(BufferedReader br) throws IOException {
		JsonObject data = new JsonObject();
		JsonArray arr = new JsonArray();
		String content = "";
		while ((content = br.readLine()) != null) {
			int start = content.indexOf("<<<") + 3;
			int end = content.indexOf(">>>");
			String key = getKey(content.substring(start, end));
			switch (key) {
			case "url":
				key = "prd_uri";
			case "img_url":
				key = "img_uri";
			case "name":
				data.putString(key, content.substring(end + 3));
				break;
			case "price":
				data.putNumber(key, Integer.parseInt(content.substring(end + 3)));
				break;
			case "ftend":
				data.putNumber("mall_idx", 3);
				data.putString("prd_uri", "asdg");
				arr.add(data);
				data = data.copy();
			default:
				continue;
			}
		}
		return arr;
	}

	private String getKey(String key) {
		switch (key) {
		case "pname":
			return "name";
		case "price":
			return key;
		case "pgurl":
			return "url";
		case "igurl":
			return "img_url";
		default:
			return key;
		}
	}
}
