package OOP.BE.datascraping.scraping.niftyGateway;

import OOP.BE.datascraping.scraping.Scraper;
import OOP.BE.datascraping.utils.USDtoETHConversion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class NiftyGateway7DScraper implements Scraper {
    @Override
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        Map<String, String> outputRows = new LinkedHashMap<>();
        final double usdToEth = USDtoETHConversion.convert();

        for (int pageNum = 1; pageNum <= 50; pageNum++) {
            final String url = "https://api.niftygateway.com/stats/rankings/?page=" + pageNum + "&page_size=50&sort=-one_day_total_volume";

            try {
                final String data = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
                JSONObject dataJson = new JSONObject(data);
                JSONArray results = (JSONArray) dataJson.get("results");

                for (Object result : results) {
                    JSONObject resultJson = (JSONObject) result;
                    JSONObject collectionJson = (JSONObject) resultJson.get("collection");
                    String name = collectionJson.get("niftyTitle")
                            .toString()
                            .replace("\"", "'")
                            .replace(":", "/")
                            .replace("\\", "/");
                    String id = collectionJson.get("niftyContractAddress").toString();
                    String image = collectionJson.get("niftyDisplayImage").toString();
                    double floorPrice = Double.parseDouble(resultJson.get("floorPrice").toString()) / usdToEth / 100.0;
                    int numOfSales = Integer.parseInt(resultJson.get("sevenDayNumTotalSales").toString());
                    int numOwners = Integer.parseInt(resultJson.get("numOwners").toString());
                    double volume = !resultJson.get("sevenDayTotalVolume").toString().equals("null") ?
                            Double.parseDouble(resultJson.get("sevenDayTotalVolume").toString()) / usdToEth / 100.0 : 0.0;
                    double volumeChange = !resultJson.get("sevenDayChange").toString().equals("null") ?
                            Double.parseDouble(resultJson.get("sevenDayChange").toString()) / 100.0 : 0.0;
                    int totalSupply = Integer.parseInt(resultJson.get("totalSupply").toString());

                    String properties =
                            "\"name\": \"" + name + "\", " +
                            "\"id\": \"" + id + "\", " +
                            "\"url\": \"" + image + "\", " +
                            "\"floorPrice\": \"" + floorPrice + "\", " +
                            "\"numOfSales\": \"" + numOfSales + "\", " +
                            "\"numOwners\": \"" + numOwners + "\", " +
                            "\"volume\": \"" + volume + "\", " +
                            "\"volumeChange\": \"" + volumeChange + "\", " +
                            "\"totalSupply\": \"" + totalSupply + "\"";

                    if (!Objects.equals(name, "") && !Objects.equals(name, "null")) {
                        outputRows.put(id, properties);
                    }
                }
                for (Map.Entry<String, String> row: outputRows.entrySet()) {
                    String valueString = '{' + row.getValue() + '}';
                    sex.put(row.getKey(), new JSONObject(valueString));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return sex;
    }
}