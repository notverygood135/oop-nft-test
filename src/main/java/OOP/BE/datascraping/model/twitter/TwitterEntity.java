package OOP.BE.datascraping.model.twitter;

import OOP.BE.datascraping.model.Entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    //"2023-11-2"
    public Date getDateTime() {
        try {
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dddd");
            Date d = fmt.parse(date);
            return d;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
