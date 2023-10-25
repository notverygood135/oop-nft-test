package datascraping.persistence;



import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static datascraping.persistence.FilePersistence.DEFAULT_PATH;

public class JsonPersistence implements DataPersistence {
    @Override
    public void save(Map<String, JSONObject> arr, String target) {
        try {
            JSONArray arrJson = new JSONArray();
            JSONParser parser = new JSONParser();
            arr.forEach((k, v) -> {
                try {
                    JSONObject outputJson = (JSONObject) parser.parse("{" + v + "}");
                    arrJson.put(outputJson);
                } catch (ParseException e) {
                    System.out.println(k + ": {" + v + "}");
                    throw new RuntimeException(e);
                }
            });
            FileWriter writer = new FileWriter(DEFAULT_PATH + target);
            writer.write(arrJson.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
