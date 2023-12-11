package OOP.BE.datascraping.dataloader;

import java.io.File;

public abstract class FileLoader<T> extends DataLoader<T> {
    protected File file;
    public FileLoader(String source) {
        super(source);
        file = new File(source);
    }
}
