package datascraping.dataloader;

import datascraping.dataloader.blog.JsonLoaderBlog;
import datascraping.dataloader.nftcollection.JsonLoaderNFTCollection;
import datascraping.dataloader.twitter.JsonLoaderTwitter;
import datascraping.model.Entity;
import datascraping.model.twitter.TwitterEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntitiesGenerator {
    private DataLoader[] loaders;
    public EntitiesGenerator(){
        loaders = new DataLoader[]{
            new JsonLoaderTwitter("twitter.json"),
            new JsonLoaderNFTCollection("binance1d.json"),
            new JsonLoaderNFTCollection("binance1d.json"),
            new JsonLoaderNFTCollection("binance7d.json"),
            new JsonLoaderNFTCollection("opensea1d.json"),
            new JsonLoaderNFTCollection("opensea7d.json"),
            new JsonLoaderNFTCollection("rarible1d.json"),
            new JsonLoaderNFTCollection("rarible7d.json"),
            new JsonLoaderNFTCollection("niftygateway1d.json"),
            new JsonLoaderNFTCollection("niftygateway7d.json"),
            new JsonLoaderBlog("nftplazas.json"),
            new JsonLoaderBlog("newbitcoins.json"),
            new JsonLoaderBlog("cointelegraph.json")
        };
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
                dem++;
                System.out.println("Thuc the thu " + dem);
                x.printDetail();
            }
            System.out.println("Thuc the: " + label + " co tong so luong la: " + dem);
        }
        return data;
    }

    public static void main(String[] args) {
        new EntitiesGenerator().generate();
    }
}
