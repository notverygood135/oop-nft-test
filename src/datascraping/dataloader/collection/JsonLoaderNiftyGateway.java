package datascraping.dataloader.collection;

import datascraping.model.CollectionEntity;
import datascraping.model.NiftyGateway;

import static datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderNiftyGateway extends JsonLoader {
    public JsonLoaderNiftyGateway(String source) {
        super(DEFAULT_PATH + source);
    }

    @Override
    protected CollectionEntity createSpecificEntity(String id, String name, String url, Double floorPrice, Double volume, Double volumeChange, Integer numOfSales, Integer numOwners, Integer totalSupply) {
        return new NiftyGateway(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
    }
}
