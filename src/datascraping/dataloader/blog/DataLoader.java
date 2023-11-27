package datascraping.dataloader.blog;

import datascraping.model.Blog;
import datascraping.model.BlogEntity;
import datascraping.model.CollectionEntity;

import java.util.Collection;

public abstract class DataLoader {
    protected String source;

    public DataLoader(String source) {
        this.source = source;
    }
    public abstract Collection<BlogEntity> load();
}
