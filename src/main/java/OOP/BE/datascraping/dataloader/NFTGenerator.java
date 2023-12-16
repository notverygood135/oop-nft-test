package OOP.BE.datascraping.dataloader;

import OOP.BE.datascraping.dataloader.nftcollection.JsonLoaderNFTCollection;
import OOP.BE.datascraping.model.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NFTGenerator {
    private DataLoader[] loaders;
    public NFTGenerator(int k){
        if(k == 0){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("binance1d.json"),
                    new JsonLoaderNFTCollection("binance7d.json"),
                    new JsonLoaderNFTCollection("opensea1d.json"),
                    new JsonLoaderNFTCollection("opensea7d.json"),
                    new JsonLoaderNFTCollection("rarible1d.json"),
                    new JsonLoaderNFTCollection("rarible7d.json"),
                    new JsonLoaderNFTCollection("niftygateway1d.json"),
                    new JsonLoaderNFTCollection("niftygateway7d.json"),
            };
        }
        else if(k == 1){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("binance1d.json"),
                    new JsonLoaderNFTCollection("opensea1d.json"),
                    new JsonLoaderNFTCollection("rarible1d.json"),
                    new JsonLoaderNFTCollection("niftygateway1d.json"),
            };
        }
        else if(k == 7){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("binance7d.json"),
                    new JsonLoaderNFTCollection("opensea7d.json"),
                    new JsonLoaderNFTCollection("rarible7d.json"),
                    new JsonLoaderNFTCollection("niftygateway7d.json"),
            };
        }
    }

    public NFTGenerator(String k){
        if(k.equals("binance") ){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("binance1d.json"),
                    new JsonLoaderNFTCollection("binance7d.json"),
            };
        }
        else if(k.equals("opensea")){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("opensea1d.json"),
                    new JsonLoaderNFTCollection("opensea7d.json"),
            };
        }
        else if(k.equals("rarible")){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("rarible1d.json"),
                    new JsonLoaderNFTCollection("rarible7d.json"),
            };
        }
        else if(k.equals("niftygateway")){
            loaders = new DataLoader[]{
                    new JsonLoaderNFTCollection("niftygateway1d.json"),
                    new JsonLoaderNFTCollection("niftygateway7d.json"),
            };
        }
    }

    public Map<String, Collection<Entity>> generate(){
        Map<String, Collection<Entity>> data = new HashMap<>();
        for(DataLoader dataLoader : loaders){
            int dem = 0;
            Collection<Entity> entities = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(10);
            data.put(label, entities);
            for(Entity x : entities){
            }
        }
        return data;
    }
}
