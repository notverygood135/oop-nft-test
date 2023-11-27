package datascraping.dataloader;

import datascraping.dataloader.collection.DataLoader;

import java.io.File;

public abstract class FileLoader extends DataLoader {

    protected File file;
    public FileLoader(String source) {
        super(source);
        file = new File(source);
    }
}
