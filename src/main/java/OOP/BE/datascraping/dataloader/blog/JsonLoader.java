package OOP.BE.datascraping.dataloader.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import OOP.BE.datascraping.dataloader.FileLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static OOP.BE.datascraping.utils.LoaderHandlers.getListAndRemove;
import static OOP.BE.datascraping.utils.LoaderHandlers.getStringAndRemove;

public abstract class JsonLoader<BlogEntity> extends FileLoader<BlogEntity> {

    public JsonLoader(String source) {
        super(source);
    }

    @Override
    public Collection<BlogEntity> load() {
        Collection<BlogEntity> blogEntities = new ArrayList<>();

        try {
            List<Map<String, Object>> dataList = new ObjectMapper().readValue(file, ArrayList.class);

            for (Map<String, Object> data : dataList) {
                String link = getStringAndRemove(data, "link");
                String img = getStringAndRemove(data, "img");
                String title = getStringAndRemove(data, "title");
                String author = getStringAndRemove(data, "author");
                String content = getStringAndRemove(data, "content");
                String date = getStringAndRemove(data, "content");
                List<String> tag = getListAndRemove(data, "tag");

                BlogEntity entity = createSpecificEntity(
                        link, img, title, author, content, date, tag
                );

                blogEntities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blogEntities;
    }

    // Abstract method for creating specific entities, to be implemented by subclasses
    protected abstract BlogEntity createSpecificEntity(
            String link, String img, String title, String content, String author, String date, List<String> tag
    );
}
