package datascraping.scraping;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RaribleScraper implements Scraper {
    @Override
    public Map<String, String> scrape() {
//    public static void main(String[] args) {

        Map<String, String> sex = new LinkedHashMap<>();
        String url = "https://rarible.com/marketplace/api/v4/collections/search";
        final double usdToEth = 1582.60;

        try {
            JSONParser parser = new JSONParser();
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
                                "\"period\":\"DAY\"," +
                                "\"hasCommunityMarketplace\":false," +
                                "\"currency\":\"NATIVE\"}}\n")
                    .ignoreContentType(true)
                    .execute()
                    .body();
            JSONArray rows = (JSONArray) parser.parse(doc);

            for (Object row : rows) {
                JSONObject rowJson = (JSONObject) row;
                JSONObject collection = (JSONObject) rowJson.get("collection");
                JSONObject statistics = (JSONObject) rowJson.get("statistics");
                JSONObject collectionStatistics = (JSONObject) collection.get("statistics");

                String name = collection.get("name").toString().replace("\"", "'");
                String id = collection.get("id").toString();

                if (statistics.get("floorPrice") == null) {
                    continue;
                }
                JSONObject floorPriceOneDay = (JSONObject) parser.parse(statistics.get("floorPrice").toString());
                double floorPrice = Double.parseDouble(floorPriceOneDay.get("value").toString());


                JSONObject usdAmount = (JSONObject) parser.parse(statistics.get("usdAmount").toString());
                double volume = Double.parseDouble(usdAmount.get("value").toString()) / usdToEth;
                double volumeChange = usdAmount.get("changePercent") != null ?
                        Double.parseDouble(usdAmount.get("changePercent").toString()) / 100.0 : 0.0;

                int numOwners = Integer.parseInt(collectionStatistics.get("ownerCountTotal").toString());
                int totalSupply = Integer.parseInt(collectionStatistics.get("itemCountTotal").toString());

                String properties =
                        "\"name\": \"" + name + "\", " +
                        "\"id\": \"" + id + "\", " +
                        "\"floorPrice\": \"" + floorPrice + "\", " +
                        "\"numOwners\": \"" + numOwners + "\", " +
                        "\"volume\": \"" + volume + "\", " +
                        "\"volumeChange\": \"" + volumeChange + "\", " +
                        "\"totalSupply\": \"" + totalSupply + "\"";

                if (!Objects.equals(name, "") && !Objects.equals(name, "null")) {
                    sex.put(id, properties);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
//        System.out.println(sex);
        return sex;
    }
}