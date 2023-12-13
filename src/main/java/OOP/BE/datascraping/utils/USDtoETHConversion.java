package OOP.BE.datascraping.utils;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;

public class USDtoETHConversion {
    public static double convert() {
        double usdToEth = 0;
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        Map<String, String> outputRows = new LinkedHashMap<>();
        String url = "https://api.coinbase.com/v2/exchange-rates?currency=ETH";
        try {
            final String json = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
            JSONObject conversion = new JSONObject(json);
            JSONObject data = (JSONObject) conversion.get("data");
            JSONObject rates = (JSONObject) data.get("rates");
            usdToEth = Double.parseDouble((String) rates.get("USD"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return usdToEth;
    }
}
