package datascraping.dataloader.collection;
import datascraping.model.CollectionEntity;
import datascraping.model.OpenSea;

import static datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderOpenSea extends JsonLoader<CollectionEntity> {
    public JsonLoaderOpenSea(String src) {
        super(DEFAULT_PATH + src);
    }

    @Override
    protected CollectionEntity createSpecificEntity(String id, String name, String url, Double floorPrice, Double volume, Double volumeChange, Integer numOfSales, Integer numOwners, Integer totalSupply) {
        return new OpenSea(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
    }
}
