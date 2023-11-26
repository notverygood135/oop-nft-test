package datascraping.scraping;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class NftPlazasScraper implements Scraper {
    @Override
    public Map<String, JSONObject> scrape() {
        final String url = "https://nftplazas.com/nft-collectibles-news/";
        Map<String, JSONObject> pageDataMap = null;
        try {
            pageDataMap = new HashMap();
            Document doc = Jsoup.connect(url).userAgent("Jsoup client").get();
            Elements links1 = doc.select(" article[class^='post_item post_layout_news-excerpt']");
            int dem=0;
            for (Element e : links1) {
                //dao o the bai viet ben ngoai
                String detailLink = e.select("a").attr("href");
                String dates = e.select("span[class='post_date post_meta_item']").text();
                String img = e.select("img").attr("src");
                String content = e.select("div[class='post_content entry-content']").text();
                String h4 = e.select("h4").text();
                JSONObject postData = new JSONObject();
                postData.put("detailLink", detailLink);
                postData.put("date", dates);
                postData.put("img", img);
                postData.put("content", content);
                postData.put("title", h4);
                //dao o trong bai viet chi tiet
                Document doc1 = Jsoup.connect(detailLink).userAgent("Jsoup client").get();
                String authors = doc1.select("meta[name='author']").attr("content");
                String tag = doc1.select("a[rel='tag']").text();
                postData.put("author", authors);
                postData.put("tag", tag);
                pageDataMap.put(detailLink, postData);
                dem++;
            }
            System.out.println("Tong so bai viet trang NFT PLAZA: " + dem);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pageDataMap;
    }
    public static void main(String[] args) {
        NftPlazasScraper nftPlazasScraper = new NftPlazasScraper();
        nftPlazasScraper.scrape();
    }
        }

