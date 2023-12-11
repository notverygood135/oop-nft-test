package OOP.BE.datascraping.scraping.rarible;

import OOP.BE.datascraping.scraping.Scraper;
import OOP.BE.datascraping.utils.USDtoETHConversion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Rarible7DScraper implements Scraper {
//    @Override
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        Map<String, String> outputRows = new LinkedHashMap<>();
        String url = "https://rarible.com/marketplace/api/v4/collections/search";
        final double usdToEth = USDtoETHConversion.convert();

        try {
            String doc = Jsoup.connect(url).method(Method.POST)
                    .userAgent("Jsoup client")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .requestBody(
                            "{\"size\":500," +
                                "\"filter\":{" +
                                "\"verifiedOnly\":false," +
                                "\"sort\":\"VOLUME_DESC\"," +
                                "\"blockchains\":[\"ETHEREUM\"]," +
                                "\"showInRanking\":false," +
                                "\"period\":\"WEEK\"," +
                                "\"hasCommunityMarketplace\":false," +
                                "\"currency\":\"NATIVE\"}}\n")
                    .ignoreContentType(true)
                    .execute()
                    .body();
            JSONArray rows = new JSONArray(doc);

            for (Object row : rows) {
                JSONObject rowJson = (JSONObject) row;
                JSONObject collection = (JSONObject) rowJson.get("collection");
                JSONArray coverMedia = (JSONArray) collection.get("coverMedia");
                JSONObject statistics = (JSONObject) rowJson.get("statistics");
                JSONObject collectionStatistics = (JSONObject) collection.get("statistics");

                String name = collection.get("name").toString().replace("\"", "'");
                String id = collection.get("id").toString();
                String image = "";
                if (!coverMedia.isEmpty()) {
                    image = coverMedia.getJSONObject(0).get("url").toString();
                }
                if (!statistics.has("floorPrice")) {
                    continue;
                }
                JSONObject floorPriceOneDay = (JSONObject) statistics.get("floorPrice");
                double floorPrice = Double.parseDouble(floorPriceOneDay.get("value").toString());

                JSONObject usdAmount = (JSONObject) statistics.get("usdAmount");
                double volume = Double.parseDouble(usdAmount.get("value").toString()) / usdToEth;
                double volumeChange = usdAmount.has("changePercent") ?
                        Double.parseDouble(usdAmount.get("changePercent").toString()) / 100.0 : 0.0;

                int numOwners = Integer.parseInt(collectionStatistics.get("ownerCountTotal").toString());
                int totalSupply = Integer.parseInt(collectionStatistics.get("itemCountTotal").toString());

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