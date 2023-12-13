package OOP.BE.datascraping.scraping.blogs;

import OOP.BE.datascraping.scraping.Scraper;
import OOP.BE.datascraping.utils.TweetHandler;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class CoinTelegraphScraper implements Scraper{
    @Override
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> pageDataMap = new HashMap<>();
        int id = 1;
        final String BASE_LINK = "https://cointelegraph.com";
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");

            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

            WebDriver driver = new FirefoxDriver(options);

            driver.get("https://cointelegraph.com/tags/nft");
            driver.getTitle();

            Thread.sleep(5000);

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            long pageHeight = (long) jsExecutor.executeScript("return Math.max( document.body.scrollHeight"
                    + ", document.body.offsetHeight, document.documentElement.clientHeight,"
                    + " document.documentElement.scrollHeight,"
                    + " document.documentElement.offsetHeight )");
            int steps = 3;
            long delayBetweenStepsInMillis = 5000;
            long scrollStep = pageHeight / steps;
            for (int i = 0; i < 100; i++) {
                    long yOffset = i * scrollStep;
                    jsExecutor.executeScript("window.scrollTo(0, " + yOffset + ")");
                    Thread.sleep(delayBetweenStepsInMillis);
            }
            String html = (String) jsExecutor.executeScript("return document.documentElement.outerHTML");
            Document document = Jsoup.parse(html);
            Elements elements = document.select("li[class='posts-listing__item'] > article[class='post-card-inline']");
            int count = 0;
            for (Element element : elements) {
                if (count++ > 300) {
                    break;
                }
                JSONObject postData = new JSONObject();
                String link = BASE_LINK + element.select("a[class='post-card-inline__figure-link']").attr("href");
                String img = element.select("img").attr("src");
                String title = element.select("span[class='post-card-inline__title']").text();
                String date = element.select("time").attr("datetime");
                String content = element.select("p[class='post-card-inline__text']").text();
                postData.put("link", link);
                postData.put("date", date);
                postData.put("img", img);
                postData.put("title", title);
                postData.put("content", content);
                crawlTags(link,driver,postData,pageDataMap);
            }
            driver.quit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return pageDataMap;
    }

    public static void crawlTags(String link, WebDriver driver, JSONObject postData, Map<String, JSONObject> pageDataMap){
        driver.get(link);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(postData.get("title"));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String html = (String) jsExecutor.executeScript("return document.documentElement.outerHTML");
        Document document = Jsoup.parse(html);
        Elements tags = document.select("li[class='tags-list__item']");
        postData.put("tag", tags.eachText());
        String author = document.select("div[class='post-meta__author-name']").text();
        postData.put("author", author);
        pageDataMap.put(link, postData);
    }
}
