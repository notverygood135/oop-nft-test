package OOP.BE.datascraping.scraping.openSea;

import OOP.BE.datascraping.scraping.Scraper;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OpenSea1DScraper implements Scraper {
    @Override
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        String url = "https://opensea.io/rankings/trending?sortBy=one_day_volume";

        try {
            final String json = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
            Document doc = Jsoup.parse(json);
            Element element = doc.select("script#__NEXT_DATA__").first();

            String data = element.data();

            JSONObject dataJson = new JSONObject(data);
            JSONObject props = (JSONObject) dataJson.get("props");
            JSONObject pageProps = (JSONObject) props.get("pageProps");
            JSONObject initialRecords = (JSONObject) pageProps.get("initialRecords");

            Map<String, String> outputRows = new LinkedHashMap<>();

            for (String s : initialRecords.keySet()) {
                JSONObject record = (JSONObject) initialRecords.get(s);
                if (record.has("name")) {
                    String id = record.get("__id").toString();
                    String name = record.get("name").toString();
                    String image = record.get("logo").toString();
                    String properties =
                            "\"name\": " + "\"" + name + "\", " +
                            "\"url\": " + "\"" + image + "\"";
                    if (!outputRows.containsKey(id)) {
                        outputRows.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    outputRows.put(id, outputRows.get(id) + ", " + properties);
                } else if (record.has("symbol")) {
                    String client = record.get("__id").toString();
                    String id = client.split(":")[1];
                    String type = client.split(":")[4];
                    String properties = "";
                    if (Objects.equals(type, "volume")) {
                        properties =
                                "\"volume\": " + "\"" + record.get("unit").toString() + "\"";
                    }
                    else if (Objects.equals(type, "floorPrice")) {
                        properties =
                                "\"floorPrice\": " + "\"" + record.get("unit").toString() + "\"";
                    }
                    if (!outputRows.containsKey(id)) {
                        outputRows.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    outputRows.put(id, outputRows.get(id) + ", " + properties);
                } else if (record.has("floorPrice")) {
                    String id = record.get("__id").toString().split(":")[1];
                    String properties =
                            "\"numOfSales\": " + "\"" + record.get("numOfSales").toString() + "\"" +
                            ", \"numOwners\": " + "\"" + record.get("numOwners").toString() + "\"" +
                            ", \"volumeChange\": " + "\"" + record.get("volumeChange").toString() + "\"" +
                            ", \"totalSupply\": " + "\"" + record.get("totalSupply").toString() + "\"";
                    if (!outputRows.containsKey(id)) {
                        outputRows.put(id, "\"id\": " + "\"" + id + "\"");
                    }
                    outputRows.put(id, outputRows.get(id) + ", " + properties);
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