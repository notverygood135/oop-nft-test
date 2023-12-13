package OOP.BE.datascraping.model.blog;

import OOP.BE.datascraping.model.Entity;

import java.util.List;

public abstract class BlogEntity implements Entity {
    private static int numEntity = 0;
    protected String link, img, title, content, author, date;
    protected List<String> tag;

    public BlogEntity(String link, String img, String title, String content, String author, String date, List<String> tag) {
        numEntity++;
        this.link = link;
        this.img = img;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.tag = tag;
    }

    public String getLink() {
        return link;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public List<String> getTag() {
        return tag;
    }

    @Override
    public void printDetail(){
        System.out.println("title: " + title + ", author: " + author + ", tags: " + tag);
    }
}
