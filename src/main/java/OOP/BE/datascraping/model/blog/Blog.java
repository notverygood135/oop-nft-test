package OOP.BE.datascraping.model.blog;

import java.util.List;

public class Blog extends BlogEntity {
    public Blog(String link, String img, String title, String content, String author, String date, List<String> tag) {
        super(link, img, title, content, author, date, tag);
    }
}
