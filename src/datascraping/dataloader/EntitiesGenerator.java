package datascraping.dataloader;

import datascraping.dataloader.blog.JsonLoaderBlog;
import datascraping.dataloader.nftcollection.JsonLoaderNFTCollection;
import datascraping.dataloader.twitter.JsonLoaderTwitter;
import datascraping.model.Entity;
import datascraping.model.twitter.TwitterEntity;

import java.util.Collection;

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
    public void generate(){
        for(DataLoader dataLoader : loaders){
            int dem = 0;
            Collection<Entity> test = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(0, loaderClassName.indexOf("Json"));

            for(Entity x : test){
                dem++;
                System.out.println("Thuc the thu " + dem);
                x.printDetail();
            }
            System.out.println("Thuc the: " + label + "co tong so luong la: " + dem);
        }
    }

    public static void main(String[] args) {
        new EntitiesGenerator().generate();
    }
}
