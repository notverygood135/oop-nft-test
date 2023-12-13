package OOP.BE.datascraping.dataloader.twitter;

import OOP.BE.datascraping.dataloader.twitter.JsonLoader;
import OOP.BE.datascraping.model.twitter.Twitter;
import OOP.BE.datascraping.model.twitter.TwitterEntity;

import java.util.List;

import static OOP.BE.datascraping.utils.Commons.DEFAULT_PATH;

public class JsonLoaderTwitter extends JsonLoader<TwitterEntity> {
    public JsonLoaderTwitter(String source) {
        super(DEFAULT_PATH + source);
    }

    @Override
    protected TwitterEntity createSpecificEntity(String date, String user, String content, List<String> hashtags) {
        return new Twitter(date, user, content, hashtags);
    }
}
