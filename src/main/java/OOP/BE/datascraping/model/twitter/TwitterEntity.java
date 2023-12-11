package OOP.BE.datascraping.model.twitter;

import OOP.BE.datascraping.model.Entity;

import java.util.List;

public class TwitterEntity implements Entity {
    protected String date, user, content;
    protected List<String> hashtags;

    public TwitterEntity(String date, String user, String content, List<String> hashtags) {
        this.date = date;
        this.user = user;
        this.content = content;
        this.hashtags = hashtags;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    @Override
    public void printDetail(){
        System.out.println("user: " + user + ", hashtags: " + hashtags);
    }
}
