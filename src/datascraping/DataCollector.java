package datascraping;

import datascraping.persistence.DataPersistence;
import datascraping.persistence.JsonPersistence;
import datascraping.scraping.*;
import org.json.JSONObject;

import java.util.Map;

public class DataCollector {
  //  private final Scraper[] scrapers;
    private final DataPersistence persistence;

    public DataCollector() {
     /*   scrapers = new Scraper[] {
                new OpenSeaScraper(),
                new NiftyGatewayScraper(),
                new BinanceScraper(),
                new RaribleScraper()
        };*/
        BlogScraper blogScraper = new NftPlazasScraper();
        persistence = new JsonPersistence();
    }

    public void run() {

      /*  for (Scraper scraper : scrapers) {

           Map<String, JSONObject> data = scraper.scrape();

            String scraperClassName = scraper.getClass().getSimpleName();
            String target = scraperClassName.substring(0, scraperClassName.indexOf("Scraper")).toLowerCase() + ".json";
            persistence.save(data, target);
        }*/
        BinanceScraper blogScraper = null;
        Map<String, JSONObject> data = blogScraper.scrape();
    }

    public static void main(String[] args) {
        new DataCollector().run();
    }
}
