package datascraping.persistence;

import java.util.Map;

public interface DataPersistence {
    void save(Map<String, String> arr, String target);
}
