package datascraping;

import datascraping.persistence.DataPersistence;
import datascraping.persistence.JsonPersistence;
import datascraping.scraping.*;

import java.util.Map;

public class DataCollector {
    private Scraper[] scrapers;
    private DataPersistence persistence;

    public DataCollector() {
        scrapers = new Scraper[] {
                new OpenSeaScraper(),
                new NiftyGatewayScraper(),
                new BinanceScraper(),
                new RaribleScraper()
        };
        persistence = new JsonPersistence();
    }

    public void run() {

        for (Scraper scraper : scrapers) {

           Map<String, String> data = scraper.scrape();

            String scraperClassName = scraper.getClass().getSimpleName();
            String target = scraperClassName.substring(0, scraperClassName.indexOf("Scraper")).toLowerCase() + ".json";
            persistence.save(data, target);
        }
    }

    public static void main(String[] args) {
        new DataCollector().run();
    }
}
