package OOP.BE.datascraping.dataloader.nftcollection;

import OOP.BE.datascraping.model.nftcollection.NFTCollection;
import OOP.BE.datascraping.model.nftcollection.NFTCollectionEntity;

import static OOP.BE.datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderNFTCollection extends JsonLoader<NFTCollectionEntity> {
    public JsonLoaderNFTCollection(String source) {
        super(DEFAULT_PATH + source);
    }

    @Override
    protected NFTCollectionEntity createSpecificEntity(String id, String name, String url, Double floorPrice, Double volume, Double volumeChange, Integer numOfSales, Integer numOwners, Integer totalSupply) {
        return new NFTCollection(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
    }
}
