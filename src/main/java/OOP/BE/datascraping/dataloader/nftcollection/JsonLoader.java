package OOP.BE.datascraping.dataloader.nftcollection;

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
                String id = getStringAndRemove(data, "id");
                String name = getStringAndRemove(data, "name");
                String url = getStringAndRemove(data, "url");
                Double floorPrice = getDoubleAndRemove(data, "floorPrice");
                Double volume = getDoubleAndRemove(data, "volume");
                Double volumeChange = getDoubleAndRemove(data, "volumeChange");
                Integer numOfSales = getIntegerAndRemove(data, "numOfSales");
                Integer numOwners = getIntegerAndRemove(data, "numOwners");
                Integer totalSupply = getIntegerAndRemove(data, "totalSupply");

                CollectionEntity entity = createSpecificEntity(
                        id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply
                );
                collectionEntities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return collectionEntities;
    }

    // Abstract method for creating specific entities, to be implemented by subclasses
    protected abstract CollectionEntity createSpecificEntity(
            String id, String name, String url, Double floorPrice, Double volume, Double volumeChange,
            Integer numOfSales, Integer numOwners, Integer totalSupply
    );
}
