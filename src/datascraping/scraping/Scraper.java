package datascraping.scraping;

import org.json.JSONObject;

import java.util.Map;

public interface Scraper {
    Map<String, JSONObject> scrape();
}
