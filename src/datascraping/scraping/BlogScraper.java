package datascraping.scraping;

import org.json.JSONObject;

import java.util.Map;

public interface BlogScraper {
    Map<String, JSONObject> scrape();
}
