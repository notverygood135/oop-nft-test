package datascraping.dataloader.blog;

import datascraping.dataloader.DataLoader;
import datascraping.model.BlogEntity;

import java.util.Collection;

public class EntitiesGenerator {
    private DataLoader<BlogEntity>[] loaders;
    public EntitiesGenerator(){
        loaders = new DataLoader[]{
            new JsonLoaderBlog("nftplazas.json"),
            new JsonLoaderBlog("newbitcoins.json"),
            new JsonLoaderBlog("cointelegraph.json")
        };
    }
    public void generate(){
        for(DataLoader<BlogEntity> dataLoader : loaders){
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
