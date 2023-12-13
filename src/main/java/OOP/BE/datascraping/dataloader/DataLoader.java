package OOP.BE.datascraping.dataloader;

import java.util.Collection;

public abstract class DataLoader<T> {
    protected String source;

    public DataLoader(String source) {
        this.source = source;
    }
    public abstract Collection<T> load();
}
