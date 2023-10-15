package datascraping.scraping;

import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BinanceScraper {
    public static void main(String[] args) {

        final String url = "https://api.niftygateway.com/stats/rankings/?page=1&page_size=50&sort=-one_day_total_volume";

        try {
            JSONParser parser = new JSONParser();
            final String json = Jsoup.connect(url).userAgent("Jsoup client").ignoreContentType(true).execute().body();
            System.out.println(json);
//            JSONObject props = (JSONObject) dataJson.get("props");
//            JSONObject pageProps = (JSONObject) props.get("pageProps");
//            JSONObject initialRecords = (JSONObject) pageProps.get("initialRecords");
//            System.out.println(initialRecords);
//            for (String s : (Iterable<String>) initialRecords.keySet()) {
//                JSONObject record = (JSONObject) initialRecords.get(s);
//                if (true) {
//                    System.out.println(record.keySet());
//                }
//            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}