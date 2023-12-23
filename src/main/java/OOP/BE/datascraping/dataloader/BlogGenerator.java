package OOP.BE.datascraping.dataloader;

import OOP.BE.datascraping.dataloader.blog.JsonLoaderBlog;
import OOP.BE.datascraping.dataloader.nftcollection.JsonLoaderNFTCollection;
import OOP.BE.datascraping.dataloader.twitter.JsonLoaderTwitter;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.twitter.TwitterEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlogGenerator {
    private DataLoader[] loaders;
    public BlogGenerator(){
        loaders = new DataLoader[]{
                new JsonLoaderBlog("nftplazas.json"),
                new JsonLoaderBlog("newbitcoins.json"),
                new JsonLoaderBlog("cointelegraph.json")
        };
    }

    public Map<String, Collection<Entity>> generate(){
        Map<String, Collection<Entity>> data = new HashMap<>();
//        int count = 0;
        for(DataLoader dataLoader : loaders){
            Collection<Entity> entities = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(10);
            if (!data.containsKey(label)) {
                data.put(label, entities);
            }
            else {
                data.get(label).addAll(entities);
            }
        }
        return data;
    }
}
