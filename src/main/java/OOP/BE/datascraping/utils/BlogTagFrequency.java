package OOP.BE.datascraping.utils;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.blog.BlogEntity;
import OOP.BE.datascraping.model.twitter.TwitterEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.*;

public class BlogTagFrequency {
    public static Map<String, Integer> calculateTagFrequency(Collection<? extends Entity> entities) {
        Map<String, Integer> tagFrequencyMap = new HashMap<>();
        for (Entity entity : entities) {
            if (entity instanceof TwitterEntity) {
                TwitterEntity twitterEntity = (TwitterEntity) entity;
                List<String> hashtags = twitterEntity.getHashtags();
                for (String hashtag : hashtags) {
                    tagFrequencyMap.put(hashtag, tagFrequencyMap.getOrDefault(hashtag, 0) + 1);
                }
            }else if(entity instanceof BlogEntity){
                BlogEntity blog  = (BlogEntity)entity;
                List<String> tags = blog.getTag();
                for (String tag : tags) {
                    tagFrequencyMap.put(tag, tagFrequencyMap.getOrDefault(tag, 0) + 1);
                }
            }
        }
        return tagFrequencyMap.entrySet()
                .stream()
                .sorted(Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
