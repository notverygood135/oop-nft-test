package datascraping.persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static datascraping.persistence.FilePersistence.DEFAULT_PATH;

public class JsonPersistence implements DataPersistence {
    @Override
    public void save(Map<String, JSONObject> arr, String target) {
        try {
            JSONArray arrJson = new JSONArray();
            arr.forEach((k, v) -> {
                arrJson.put(v);
            });
            FileWriter writer = new FileWriter(DEFAULT_PATH + target);
            writer.write(arrJson.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
