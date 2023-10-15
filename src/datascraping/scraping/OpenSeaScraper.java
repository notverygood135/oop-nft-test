package datascraping.scraping;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;

public class OpenSeaScraper implements Scraper {
    @Override
    public Map<String, String> scrape() {
        Map<String, String> sex = new LinkedHashMap<>();

        try {
            JSONParser parser = new JSONParser();
            String url = "https://opensea.io/rankings/trending";
            final String json = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
            Document doc = Jsoup.parse(json);
            Element element = doc.select("script#__NEXT_DATA__").first();

            String data = element.data();
            JSONObject dataJson = (JSONObject) parser.parse(data);
            JSONObject props = (JSONObject) dataJson.get("props");
            JSONObject pageProps = (JSONObject) props.get("pageProps");
            JSONObject initialRecords = (JSONObject) pageProps.get("initialRecords");

            for (String s : (Iterable<String>) initialRecords.keySet()) {
                JSONObject record = (JSONObject) initialRecords.get(s);
                if (record.containsKey("name")) {
                    String id = record.get("__id").toString();
                    String properties =
                            "\"name\": " + "\"" + record.get("name").toString() + "\"" +
                                    ", \"createdDate\": " + "\"" + record.get("createdDate").toString() + "\"";
                    if (!sex.containsKey(id)) {
                        sex.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    sex.put(id, sex.get(id) + ", " + properties);
                } else if (record.containsKey("symbol")) {
                    String id = record.get("__id").toString().split(":")[1];
                    String properties =
                            "\"unit\": " + "\"" + record.get("unit").toString() + "\"";
                    if (!sex.containsKey(id)) {
                        sex.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    sex.put(id, sex.get(id) + ", " + properties);
                } else if (record.containsKey("floorPrice")) {
                    String id = record.get("__id").toString().split(":")[1];
                    String properties =
                            "\"numOfSales\": " + "\"" + record.get("numOfSales").toString() + "\"" +
                                    ", \"numOwners\": " + "\"" + record.get("numOwners").toString() + "\"" +
                                    ", \"volumeChange\": " + "\"" + record.get("volumeChange").toString() + "\"" +
                                    ", \"totalSupply\": " + "\"" + record.get("totalSupply").toString() + "\"" +
                                    ", \"totalListed\": " + "\"" + record.get("totalListed").toString() + "\"";
                    if (!sex.containsKey(id)) {
                        sex.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    sex.put(id, sex.get(id) + ", " + properties);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return sex;
    }
}