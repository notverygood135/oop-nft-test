package datascraping.dataloader.twitter;

import datascraping.dataloader.twitter.JsonLoader;
import datascraping.model.twitter.Twitter;
import datascraping.model.twitter.TwitterEntity;

import java.util.List;

import static datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderTwitter extends JsonLoader<TwitterEntity> {
    public JsonLoaderTwitter(String source) {
        super(DEFAULT_PATH + source);
    }

    @Override
    protected TwitterEntity createSpecificEntity(String date, String user, String content, List<String> hashtags) {
        return new Twitter(date, user, content, hashtags);
    }
}
