package datascraping.dataloader.blog;

import datascraping.model.blog.Blog;
import datascraping.model.blog.BlogEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderBlog extends JsonLoader<BlogEntity> {
    public JsonLoaderBlog(String path) {
        super(DEFAULT_PATH +path);
    }

    @Override
    protected BlogEntity createSpecificEntity(
            String link, String img, String title, String content, String author, String date, List<String> tag
    ) {
        return new Blog(link, img, title, content, author, date, tag);
    }

    public static void main(String[] args) {
        JsonLoaderBlog nftPlazas = new JsonLoaderBlog("nftplazas.json");
        Collection<BlogEntity> test = new ArrayList<>();
        for(BlogEntity x : test){
            x.printDetail();
        }
    }
}



