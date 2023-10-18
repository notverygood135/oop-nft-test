package datascraping.scraping;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class NiftyGatewayScraper implements Scraper{
    @Override
    public Map<String, String> scrape() {
        Map<String, String> sex = new LinkedHashMap<>();
        final double usdToEth = 1582.60;

        for (int pageNum = 1; pageNum <= 50; pageNum++) {
            final String url = "https://api.niftygateway.com/stats/rankings/?page=" + pageNum + "&page_size=50&sort=-one_day_total_volume";

            try {
                JSONParser parser = new JSONParser();
                final String json = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
                JSONObject dataJson = (JSONObject) parser.parse(json);
                JSONArray results = (JSONArray) dataJson.get("results");

                for (Object result : results) {
                    JSONObject resultJson = (JSONObject) result;
                    JSONObject collectionJson = (JSONObject) resultJson.get("collection");
                    String name = collectionJson.get("niftyTitle").toString().replace("\"", "'");
                    String id = collectionJson.get("niftyContractAddress").toString();
                    double floorPrice = Double.parseDouble(resultJson.get("floorPrice").toString()) / usdToEth / 100.0;
                    int numOfSales = Integer.parseInt(resultJson.get("oneDayNumTotalSales").toString());
                    int numOwners = Integer.parseInt(resultJson.get("numOwners").toString());
                    double volume = resultJson.get("oneDayTotalVolume") != null ?
                            Double.parseDouble(resultJson.get("oneDayTotalVolume").toString()) / usdToEth / 100.0 : 0.0;
                    double volumeChange = resultJson.get("oneDayChange") != null ?
                            Double.parseDouble(resultJson.get("oneDayChange").toString()) / 100.0 : 0.0;
                    int totalSupply = Integer.parseInt(resultJson.get("totalSupply").toString());

                    String properties =
                            "\"name\": \"" + name + "\", " +
                            "\"id\": \"" + id + "\", " +
                            "\"floorPrice\": \"" + floorPrice + "\", " +
                            "\"numOfSales\": \"" + numOfSales + "\", " +
                            "\"numOwners\": \"" + numOwners + "\", " +
                            "\"volume\": \"" + volume + "\", " +
                            "\"volumeChange\": \"" + volumeChange + "\", " +
                            "\"totalSupply\": \"" + totalSupply + "\"";

                    if (!Objects.equals(name, "") && !Objects.equals(name, "null")) {
                        sex.put(id, properties);
                        System.out.println(id + ": {" + properties + "}");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return sex;
    }
}