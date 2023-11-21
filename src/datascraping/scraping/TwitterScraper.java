package datascraping.scraping;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsSearchAllResponse;
import com.twitter.clientlib.model.Get2TweetsSearchRecentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.util.*;

public class TwitterScraper {
    public static void main(String[] args) throws ApiException {
//        TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
//                System.getenv("TWITTER_OAUTH2_CLIENT_ID"),
//                System.getenv("TWITTER_OAUTH2_CLIENT_SECRET"),
//                System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN"),
//                System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN_SECRET"))
//        );
        TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsBearer(
                System.getenv("TWITTER_BEARER_TOKEN")
        ));

        String query = "nft";
        int maxResults = 100;

        Set<String> tweetFields = new HashSet<>();
        tweetFields.add("hashtags");

        Get2TweetsSearchRecentResponse result =
                apiInstance.tweets().tweetsRecentSearch(query).maxResults(100)
                        .tweetFields(tweetFields)
                        .excludeInputFields().execute();

        System.out.println(result);
    }
}
