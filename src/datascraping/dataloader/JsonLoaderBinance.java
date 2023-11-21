package datascraping.dataloader;
import datascraping.model.Binance;
import datascraping.model.CollectionEntity;

import java.util.Collection;


import static datascraping.utils.Commons.DEFAULT_PATH;
public class JsonLoaderBinance extends JsonLoader {
    public JsonLoaderBinance(String path) {
        super(DEFAULT_PATH +path);}
        @Override
        protected CollectionEntity createSpecificEntity (String id, String name, String url, Double floorPrice, Double
        volume, Double volumeChange, Integer numOfSales, Integer numOwners, Integer totalSupply){
            return new Binance(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
        }
    public static void main(String[] args) {
        JsonLoaderBinance jsonLoaderBinance = new JsonLoaderBinance("binance1d.json");
        Collection<CollectionEntity> test = jsonLoaderBinance.load();
        for(CollectionEntity x:test){
            x.printDetail();
        }
    }
    }



