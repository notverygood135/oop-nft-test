package datascraping.dataloader.blog;

import datascraping.dataloader.collection.JsonLoaderBinance;
import datascraping.dataloader.collection.JsonLoaderNiftyGateway;
import datascraping.dataloader.collection.JsonLoaderOpenSea;
import datascraping.dataloader.collection.JsonLoaderRarible;
import datascraping.model.BlogEntity;
import datascraping.model.CollectionEntity;

import java.util.Collection;

public class EntitiesGenerator {
    private DataLoader[] loaders;
    public EntitiesGenerator(){
        loaders = new DataLoader[]{
            new JsonLoaderBlog("nftplazas.json"),
            new JsonLoaderBlog("newbitcoins.json"),
            new JsonLoaderBlog("cointelegraph.json")
        };
    }
    public void generate(){
        for(DataLoader dataLoader : loaders){
            int dem = 0;
            Collection<BlogEntity> test = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(0, loaderClassName.indexOf("Json"));

            for(BlogEntity x : test){
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
