package OOP.BE.datascraping;

import OOP.BE.datascraping.persistence.DataPersistence;
import OOP.BE.datascraping.persistence.JsonPersistence;
import OOP.BE.datascraping.scraping.*;
import OOP.BE.datascraping.scraping.binance.Binance1DScraper;
import OOP.BE.datascraping.scraping.binance.Binance7DScraper;
import OOP.BE.datascraping.scraping.blogs.CoinTelegraphScraper;
import OOP.BE.datascraping.scraping.blogs.NewBitcoinsScraper;
import OOP.BE.datascraping.scraping.blogs.NftPlazasScraper;
import OOP.BE.datascraping.scraping.niftyGateway.NiftyGateway1DScraper;
import OOP.BE.datascraping.scraping.niftyGateway.NiftyGateway7DScraper;
import OOP.BE.datascraping.scraping.openSea.OpenSea1DScraper;
import OOP.BE.datascraping.scraping.openSea.OpenSea7DScraper;
import OOP.BE.datascraping.scraping.rarible.Rarible1DScraper;
import OOP.BE.datascraping.scraping.rarible.Rarible7DScraper;
import OOP.BE.datascraping.scraping.twitter.TwitterScraper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DataCollector {
    private final Scraper[] scrapers;
    private final DataPersistence persistence;

    public DataCollector() {
        scrapers = new Scraper[] {
                new OpenSea1DScraper(),
                new NiftyGateway1DScraper(),
                new Binance1DScraper(),
                new Rarible1DScraper(),
                new OpenSea7DScraper(),
                new NiftyGateway7DScraper(),
                new Binance7DScraper(),
                new Rarible7DScraper(),
                new TwitterScraper(),
                new NftPlazasScraper(),
                new NewBitcoinsScraper(),
                new CoinTelegraphScraper()
        };

        persistence = new JsonPersistence();
    }

    public void run() {

        for (Scraper scraper : scrapers) {
            Map<String, JSONObject> data = null;
            try {
                data = scraper.scrape();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String scraperClassName = scraper.getClass().getSimpleName();
            String target = scraperClassName.substring(0, scraperClassName.indexOf("Scraper")).toLowerCase() + ".json";
            persistence.save(data, target);
            System.out.println(scraperClassName + data.size());
        }
    }

    public static void main(String[] args) {
        new DataCollector().run();
    }
}
