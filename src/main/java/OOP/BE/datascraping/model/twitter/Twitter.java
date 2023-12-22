package OOP.BE.datascraping.model.twitter;

import java.util.List;

public class Twitter extends TwitterEntity {

    // Default constructor
    public Twitter() {
        super(null, null, null, null);
    }

    public Twitter(String date, String user, String content, List<String> hashtags) {
        super(date, user, content, hashtags);
    }

    @Override
    public String toString() {
        // Customize this method based on how you want to display Twitter content
        return "Date: " + getDate() + "\nUser: " + getUser() + "\nContent: " + getContent() + "\nHashtags: " + getHashtags();
    }
}

