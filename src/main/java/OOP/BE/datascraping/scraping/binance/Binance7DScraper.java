package OOP.BE.datascraping.scraping.binance;

import OOP.BE.datascraping.scraping.Scraper;
import OOP.BE.datascraping.utils.USDtoETHConversion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Binance7DScraper implements Scraper {
    @Override
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        Map<String, String> outputRows = new LinkedHashMap<>();
        String url = "https://www.binance.com/bapi/nft/v1/friendly/nft/ranking/trend-collection";
        final double usdToEth = USDtoETHConversion.convert();

        try {
            String doc = Jsoup.connect(url).method(Method.POST)
                    .userAgent("Jsoup client")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .requestBody("{\"network\":\"ALL\",\"period\":\"7D\",\"sortType\":\"volumeDesc\",\"page\":1,\"rows\":100}")
                    .ignoreContentType(true)
                    .execute()
                    .body();
            JSONObject json = new JSONObject(doc);
            JSONObject data = (JSONObject) json.get("data");
            JSONArray rows = (JSONArray) data.get("rows");

            for (Object row : rows) {
                JSONObject rowJson = (JSONObject) row;
                String name = rowJson.get("title").toString().replace("\"", "'");
                String id = rowJson.get("collectionId").toString();
                String image = rowJson.get("coverUrl").toString();
                double floorPrice = Double.parseDouble(rowJson.get("floorPrice").toString());
                int numOwners = Integer.parseInt(rowJson.get("ownersCount").toString());
                double volume = Double.parseDouble(rowJson.get("volume").toString()) / usdToEth / 100.0;
                double volumeChange = Double.parseDouble(rowJson.get("volumeRate").toString()) / 100.0;
                int totalSupply = Integer.parseInt(rowJson.get("itemsCount").toString());

                String properties =
                        "\"name\": \"" + name + "\", " +
                        "\"id\": \"" + id + "\", " +
                        "\"url\": \"" + image + "\", " +
                        "\"floorPrice\": \"" + floorPrice + "\", " +
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return sex;
    }
}