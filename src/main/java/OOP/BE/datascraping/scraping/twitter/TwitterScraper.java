package OOP.BE.datascraping.scraping.twitter;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TwitterScraper implements Scraper {
    public Map<String, JSONObject> scrape() {
        Map<String, JSONObject> sex = new LinkedHashMap<>();
        int id = 1;
        try {
            FirefoxOptions options = new FirefoxOptions();
//            options.addArguments("--headless");

            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

            WebDriver driver = new FirefoxDriver(options);

            driver.get("https://twitter.com/login");
            driver.getTitle();

            Thread.sleep(5000);

            WebElement usernameInput = driver.findElement(  // Tìm đến phần tử có thẻ input và có 2 thuộc tính autocomplete và name
                    By.cssSelector("input[autocomplete='username'][name='text']"));
            usernameInput.sendKeys("nguyenvanphuanak30@gmail.com"); //Set cho phần tử đó có giá trị là tài khoản đăng nhập
            usernameInput.sendKeys(Keys.ENTER);     //Ấn Enter để chuyển trang tùy vào trang có thể là ấn Enter có thể là button ấn nhưng 90% là enter
            Thread.sleep(5000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Đợi 10s load web trong trường hợp web có js ẩn div hoặc các element khác

            WebElement phone = wait.until(ExpectedConditions.presenceOfElementLocated( // Tìm phần tử có password như trên sau đó gửi mật khẩu của mình vào
                    By.cssSelector("input[autocomplete='on'][name='text']")));
            phone.sendKeys("0974769722");
            phone.sendKeys(Keys.ENTER);
            Thread.sleep(5000); // Đợi 5s để đăng nhập

            WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated( // Tìm phần tử có password như trên sau đó gửi mật khẩu của mình vào
                    By.cssSelector("input[autocomplete='current-password'][name='password']")));
            password.sendKeys("chipsaorua123");
            password.sendKeys(Keys.ENTER);
            Thread.sleep(5000); // Đợi 5s để đăng nhập

            driver.navigate().to("https://twitter.com/search?q=%23nft&src=typed_query&f=top"); //Navigate hay chuyển hướng đến trang web cần sau khi đăng nhập do đã có cookie lên có thể lấy được dễ dàng

            Thread.sleep(5000); //Đợi 5s để navigate ổn định

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            long pageHeight = (long) jsExecutor.executeScript("return Math.max( document.body.scrollHeight"
                    + ", document.body.offsetHeight, document.documentElement.clientHeight,"
                    + " document.documentElement.scrollHeight,"
                    + " document.documentElement.offsetHeight )");
            int steps = 1;
            long delayBetweenStepsInMillis = 5000;
            long scrollStep = pageHeight / steps;
            for (int i = 0; i < 20; i++) {
                String html = (String) jsExecutor.executeScript("return document.documentElement.outerHTML");
                Document document = Jsoup.parse(html);
                Elements elements = document.select("div[class='css-175oi2r r-1iusvr4 r-16y2uox r-1777fci r-kzbkwu']");
                for (Element element : elements) {
                    sex.put(String.valueOf(id++), TweetHandler.handle(element));
                    long yOffset = i * scrollStep;
                    jsExecutor.executeScript("window.scrollTo(0, " + yOffset + ")");
                    Thread.sleep(delayBetweenStepsInMillis);
                }
            }
            driver.quit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return sex;
    }
}
