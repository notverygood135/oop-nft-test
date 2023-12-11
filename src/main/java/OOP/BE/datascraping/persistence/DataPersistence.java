package OOP.BE.datascraping.persistence;

import org.json.JSONObject;

import java.util.Map;

public interface DataPersistence {
    void save(Map<String, JSONObject> arr, String target);
}
