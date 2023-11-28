package datascraping.dataloader.twitter;

import datascraping.dataloader.DataLoader;
import datascraping.model.twitter.TwitterEntity;

import java.util.Collection;

public class EntitiesGenerator {
    private DataLoader[] loaders;
    public EntitiesGenerator(){
        loaders = new DataLoader[]{
            new JsonLoaderTwitter("twitter.json"),
        };
    }
    public void generate(){
        for(DataLoader dataLoader : loaders){
            int dem = 0;
            Collection<TwitterEntity> test = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(0, loaderClassName.indexOf("Json"));

            for(TwitterEntity x : test){
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
