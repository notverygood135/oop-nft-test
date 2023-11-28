package datascraping.dataloader;

import datascraping.model.Blog;
import datascraping.model.BlogEntity;
import datascraping.model.CollectionEntity;

import java.util.Collection;

public abstract class DataLoader<T> {
    protected String source;

    public DataLoader(String source) {
        this.source = source;
    }
    public abstract Collection<T> load();
}
