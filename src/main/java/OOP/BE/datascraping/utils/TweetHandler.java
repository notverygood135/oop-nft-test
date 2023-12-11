package OOP.BE.datascraping.utils;

import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.ListIterator;

public class TweetHandler {
    public static JSONObject handle(Element element) {
        JSONObject jsonObject;
        String user = element.select("div[class='css-175oi2r r-1awozwy r-18u37iz r-1wbh5a2 r-dnmrzs'] span[class^='css-1qaijid r-bcqeeo r-qvutc0']").text().replace("\"", "'").replace("\\", "|");
        String content = element.select("div[data-testid='tweetText'] span[class^='css-1qaijid r-bcqeeo r-qvutc0']").text().replace("\"", "'").replace("\\", "|");
        List<String> hashtags = element.select("a[href^='/hashtag/']").eachText();
        for (ListIterator<String> hashtag = hashtags.listIterator(); hashtag.hasNext();) {
            hashtag.set("\"" + hashtag.next() + "\"");
        }
        String date = element.select("time").attr("datetime").substring(0, 9);
        System.out.println("{user: \"" + user + "\", content: \"" + content + "\", hashtags: " + hashtags + ", date: " + date + "}");
        jsonObject = new JSONObject("{user: \"" + user + "\", content: \"" + content + "\", hashtags: " + hashtags + ", date: " + date + "}");
        return jsonObject;
    }
}
