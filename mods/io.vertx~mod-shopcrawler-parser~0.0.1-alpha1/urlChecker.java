import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class urlChecker extends Verticle {

    static EventBus eventBus;
    
    /**
     * url connection timeout
     */
    final static private int connectionTimeout = 5000;
    
    /**
     * solution list
     */
    public enum SHOPPING_MALL_TYPE {
        GABIA, GODO, MAKESHOP, MALLFORYOU, BLUEWEB, INTORE, CAFE24, WHOIS, NTSOFT, NICECART, DREAMWEB, WISA, ETC
    };
    
    /**
     * @todo export static file or DB
     */
    private Map<SHOPPING_MALL_TYPE, String> dbUrl = null; {
        dbUrl = new HashMap<SHOPPING_MALL_TYPE, String>();
        dbUrl.put(SHOPPING_MALL_TYPE.GABIA,      "/naver_shop_list_sum.html");
        dbUrl.put(SHOPPING_MALL_TYPE.GODO,       "/shop/compare/compare_list.php?pKey=novelty&engine=naverep3");
        dbUrl.put(SHOPPING_MALL_TYPE.MAKESHOP,   "/list/navernewopen.html?type=summary");
        dbUrl.put(SHOPPING_MALL_TYPE.MALLFORYOU, "/mallsarang/acecart/bin/naver_list3.php?mode=abstract");
        dbUrl.put(SHOPPING_MALL_TYPE.BLUEWEB,    "/letsmall/cpprice/naver_brain.php?opt=2");
        dbUrl.put(SHOPPING_MALL_TYPE.INTORE,     "/compare/naver_db_sum.html");
        dbUrl.put(SHOPPING_MALL_TYPE.CAFE24,     "/web/ghost_mall/naver_shop_summary.com.html");
        dbUrl.put(SHOPPING_MALL_TYPE.WHOIS,      "/FrontStore/__ipcs__/IPrice/navershop_summary.phtml");
        dbUrl.put(SHOPPING_MALL_TYPE.NTSOFT,     "/talent/cpprice/naver_brain.php?opt=2");
        dbUrl.put(SHOPPING_MALL_TYPE.NICECART,   "/Nicecart4_new/Naverengine/new_enginedb_2.php");
        dbUrl.put(SHOPPING_MALL_TYPE.DREAMWEB,   "/price_link/naver_sprice_new.php");
        dbUrl.put(SHOPPING_MALL_TYPE.WISA,       "/_data/compare/naver/summary_prd.txt");
        dbUrl.put(SHOPPING_MALL_TYPE.ETC, null);
    }

    /**
     * @todo Exception
     */
    public void start() {
        eventBus = vertx.eventBus();
        Handler<Message> handler = new Handler<Message>() {
            @Override
            public void handle(Message message) {
                String stringData = message.toString();
                int firstEOL = stringData.indexOf(System.getProperty("line.separator"));
                firstEOL = (firstEOL==-1)? stringData.indexOf('\n'): firstEOL;
                String url = stringData.substring(0, firstEOL);
                String htmlData = stringData.substring(firstEOL);
                
                try {
                    parseData(url, htmlData);
                } catch (Exception e) {
//					e.printStackTrace();
				}
            }
        };
        eventBus.registerHandler("shop.parse.parse", handler);
    }
    
    /**
     * @todo classloader -> vert.x
     */
    private void parseData(String url, String html) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SHOPPING_MALL_TYPE shopType = getShopType(url);
        String className = shopType.toString().toLowerCase()+"Parser";
        parser newInstance = (parser) parser.class.getClassLoader().loadClass(className).newInstance();
        newInstance.parse(url, html);
    }
    
    private SHOPPING_MALL_TYPE getShopType(String url) throws MalformedURLException {
        String baseUrl = getBaseUrl(url);
        for (SHOPPING_MALL_TYPE type : SHOPPING_MALL_TYPE.values()) {
            if (checkUrlConnection(getDBurlAddress(type, baseUrl))) return type; 
        }
        return SHOPPING_MALL_TYPE.ETC;
    }
    
    /**
     * @param fullUrl
     * @return protocol + hostname 
     * @throws MalformedURLException
     */
    private String getBaseUrl(String fullUrl) throws MalformedURLException {
        URL url = new URL(fullUrl);
        return url.getProtocol() + "://" + url.getHost();
    }
    
    /**
     * check url connection
     * @param url
     * @return connection status or valid url
     */
    private boolean checkUrlConnection(String url) {
        BufferedReader br = null;
        int responseCode = 0,
            startTagIndex = -1;
        try {
            URL checkUrl = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) checkUrl.openConnection();
            huc.setConnectTimeout(connectionTimeout); 
            huc.setReadTimeout(connectionTimeout); 
            huc.setRequestMethod("GET");
            huc.connect();
            responseCode = huc.getResponseCode();
            br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            startTagIndex = br.readLine().indexOf("<<<");
            huc.disconnect();
        } catch (Exception e) {
            return false;
        }
        return (responseCode == 200 && startTagIndex >= 0);
    }
    
    /**
     * return solution's db-url address
     * @param type
     * @param baseUrl
     * @return url
     */
    private String getDBurlAddress(SHOPPING_MALL_TYPE type, String baseUrl) {
        return baseUrl + dbUrl.get(type);
    }
}
