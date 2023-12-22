package OOP.BE.datascraping.model.blog;

import java.util.List;

public class Blog extends BlogEntity {
    public Blog(String link, String img, String title, String content, String author, String date, List<String> tag) {
        super(link, img, title, content, author, date, tag);
    }

    @Override
    public String toString() {
        // Customize this method based on how you want to display Twitter content
        return "Date: " + getDate() + "\n" +
                "Author: " + getAuthor() + "\n" +
                "Title: " + getTitle() + "\n" +
                "Content: " + getContent() + "\n" +
                "Tags: " + getTag() + "\n" +
                "Read more: " + getLink();
    }
}
