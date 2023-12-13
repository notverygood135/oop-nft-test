package OOP.BE.datascraping.model.twitter;

import java.util.List;

public class Twitter extends TwitterEntity {

    public Twitter(String date, String user, String content, List<String> hashtags) {
        super(date, user, content, hashtags);
    }
}
