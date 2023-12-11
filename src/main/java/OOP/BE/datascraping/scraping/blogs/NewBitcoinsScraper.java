package OOP.BE.datascraping.scraping.blogs;
import OOP.BE.datascraping.scraping.Scraper;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;
import static java.lang.Thread.*;

public class NewBitcoinsScraper implements Scraper {
    public static final int PAGE_NUMBER = 2;
    @Override
    public Map<String, JSONObject> scrape()  {
        Map<String, JSONObject> pageDataMap = new HashMap<>();
        try {

            FirefoxOptions options = new FirefoxOptions();
//            options.addArguments("--headless");

            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

            WebDriver driver = new FirefoxDriver(options);
            int dem=0;
            for(int i = 1; i <= PAGE_NUMBER; i++){
                driver.get("https://news.bitcoin.com/tag/nft-collection/page/" + i);
                sleep(3000);
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                String html = (String) jsExecutor.executeScript("return document.documentElement.outerHTML");
                Document document = Jsoup.parse(html);
                Elements elements = document.select("div[class='sc-fFDWmC ckwugE']");

                for(Element e : elements){
                    JSONObject postData = new JSONObject();
                    String link = "https://news.bitcoin.com" + e.selectFirst("a").attr("href");
                    String img = e.select("img").attr("src");
                    String title = e.select("h6").text();
                    String dateContent = e.select("p[class='sc-dSJDGZ fBa-dYU']").text();
                    String date = extractDate(dateContent);
                    postData.put("link", link);
                    postData.put("date", date);
                    postData.put("img", img);
                    postData.put("title", title);
                    crawlTags(link,driver,postData,pageDataMap);
                    dem++;
                }
            }
            System.out.println("Tong so bai viet trang New Bitcoins: " + dem);
            driver.quit();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return pageDataMap;
    }
    public void crawlTags(String link,WebDriver driver,JSONObject postData,Map<String, JSONObject> pageDataMap){
        driver.get(link);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String html = (String) jsExecutor.executeScript("return document.documentElement.outerHTML");
        Document document = Jsoup.parse(html);
        Elements e = document.select("div[class='sc-kjaWZf gpmoND']");
        String content = document.select("strong").text();
        postData.put("content", content);
        postData.put("tag", e.eachText());
        String author = document.select("h5[class='sc-cydatY sc-eVmQbm eFDwpm fwPqpt']").text();
        postData.put("author", author);
        pageDataMap.put(link,postData);
    }

    private static String extractDate(String input) {
        String[] parts = input.split("\\|");
        if (parts.length >= 2) {
            return parts[1].trim();
        } else {
            return null;
        }
    }
}
