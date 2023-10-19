package datascraping.scraping;

import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jsoup.nodes.Document;

public class NftPlazasScraper{
    public static void main(String[] args) {
//        Map<String, String> sex = new LinkedHashMap<>();
//        final double usdToEth = 1582.60;

        final String url = "https://nftplazas.com/nft-collectibles-news/";
        try {
            JSONParser parser = new JSONParser();
            Document doc = Jsoup.connect(url).userAgent("Jsoup client").get();
            Elements sex = doc.select("article > a");
            for (Element element : sex) {
                System.out.println(element);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}