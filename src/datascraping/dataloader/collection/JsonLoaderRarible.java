package datascraping.dataloader.collection;
import datascraping.model.CollectionEntity;
import datascraping.model.Rarible;

import static datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderRarible extends JsonLoader<CollectionEntity> {

    public JsonLoaderRarible(String source) {
        super(DEFAULT_PATH + source);
    }

    @Override
    protected CollectionEntity createSpecificEntity(String id, String name, String url, Double floorPrice, Double volume, Double volumeChange, Integer numOfSales, Integer numOwners, Integer totalSupply) {
        return new Rarible(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
    }
}
