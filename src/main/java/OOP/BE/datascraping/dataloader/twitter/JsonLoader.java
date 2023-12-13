package OOP.BE.datascraping.dataloader.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import OOP.BE.datascraping.dataloader.FileLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static OOP.BE.datascraping.utils.LoaderHandlers.*;

public abstract class JsonLoader<CollectionEntity> extends FileLoader<CollectionEntity> {

    public JsonLoader(String source) {
        super(source);
    }

    @Override
    public Collection<CollectionEntity> load() {
        Collection<CollectionEntity> collectionEntities = new ArrayList<>();

        try {
            List<Map<String, Object>> dataList = new ObjectMapper().readValue(file, ArrayList.class);

            for (Map<String, Object> data : dataList) {
                String date = getStringAndRemove(data, "date");
                String user = getStringAndRemove(data, "user");
                String content = getStringAndRemove(data, "content");
                List<String> hashtags = getListAndRemove(data, "hashtags");

                CollectionEntity entity = createSpecificEntity(
                        date, user, content, hashtags
                );
                collectionEntities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return collectionEntities;
    }

    // Abstract method for creating specific entities, to be implemented by subclasses
    protected abstract CollectionEntity createSpecificEntity(String date, String user, String content, List<String> hashtags);
}
