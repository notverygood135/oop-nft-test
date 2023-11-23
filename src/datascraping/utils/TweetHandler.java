package datascraping.utils;

import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.ListIterator;

public class TweetHandler {
    public static JSONObject handle(Element element) {
        JSONObject jsonObject;
        String user = element.select("div[class='css-1rynq56 r-bcqeeo r-qvutc0 r-1tl8opc r-a023e6 r-rjixqe r-b88u0q r-1awozwy r-6koalj r-1udh08x r-3s2u2q'] span[class='css-1qaijid r-bcqeeo r-qvutc0 r-1tl8opc']").text().replace("\"", "'").replace("\\", "|");
        String content = element.select("div[data-testid=\"tweetText\"] span[class='css-1qaijid r-bcqeeo r-qvutc0 r-1tl8opc']").text().replace("\"", "'").replace("\\", "|");
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
