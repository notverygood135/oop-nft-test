package OOP.BE.datascraping.scraping.blogs;
import OOP.BE.datascraping.scraping.Scraper;
import org.json.JSONArray;
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
            Elements elements = doc.select(" article[class^='post_item post_layout_news-excerpt']");
            int dem=0;
            for (Element e : elements) {
                //dao o the bai viet ben ngoai
                String link = e.select("a").attr("href");
                String date = e.select("span[class='post_date post_meta_item']").text();
                String img = e.select("img").attr("src");
                String content = e.select("div[class='post_content entry-content']").text();
                String title = e.select("h4").text();
                JSONObject postData = new JSONObject();
                postData.put("link", link);
                postData.put("date", date);
                postData.put("img", img);
                postData.put("content", content);
                postData.put("title", title);
                //dao o trong bai viet chi tiet
                Document doc1 = Jsoup.connect(link).userAgent("Jsoup client").get();
                String authors = doc1.select("meta[name='author']").attr("content");
                JSONArray tag = new JSONArray();
                tag.put(doc1.select("a[rel='tag']").text());
                postData.put("author", authors);
                postData.put("tag", tag);
                pageDataMap.put(link, postData);
                dem++;
            }
            System.out.println("Tong so bai viet trang NFT PLAZA: " + dem);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pageDataMap;
    }
}
