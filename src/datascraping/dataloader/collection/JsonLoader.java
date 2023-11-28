package datascraping.dataloader.collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import datascraping.dataloader.FileLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class JsonLoader<CollectionEntity> extends FileLoader<CollectionEntity> {

    public JsonLoader(String source) {
        super(source);
    }

    @Override
    public Collection<CollectionEntity> load() {
        Collection<CollectionEntity> collectionEntities = new ArrayList<>();

        try {
            List<Map<String, Object>> dataList = new ObjectMapper().readValue(file, ArrayList.class);

            for (Map<String, Object> data : dataList) {
                String id = getStringAndRemove(data, "id");
                String name = getStringAndRemove(data, "name");
                String url = getStringAndRemove(data, "url");
                Double floorPrice = getDoubleAndRemove(data, "floorPrice");
                Double volume = getDoubleAndRemove(data, "volume");
                Double volumeChange = getDoubleAndRemove(data, "volumeChange");
                Integer numOfSales = getIntegerAndRemove(data, "numOfSales");
                Integer numOwners = getIntegerAndRemove(data, "numOwners");
                Integer totalSupply = getIntegerAndRemove(data, "totalSupply");

                CollectionEntity entity = createSpecificEntity(
                        id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply
                );
                collectionEntities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return collectionEntities;
    }

    // Abstract method for creating specific entities, to be implemented by subclasses
    protected abstract CollectionEntity createSpecificEntity(
            String id, String name, String url, Double floorPrice, Double volume, Double volumeChange,
            Integer numOfSales, Integer numOwners, Integer totalSupply
    );

    protected String getStringAndRemove(Map<String, Object> data, String key) {
        String value = (String) data.get(key);
        data.remove(key);
        return value;
    }

    protected Double getDoubleAndRemove(Map<String, Object> data, String key) {
        Object value = data.get(key);
        data.remove(key);

        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                // Xử lý nếu không thể chuyển đổi thành Double
                // Ví dụ: có thể trả về giá trị mặc định hoặc thông báo lỗi
                return 0.0; // Thay defaultValue bằng giá trị mặc định bạn muốn sử dụng
            }
        } else {
            // Xử lý các trường hợp khác nếu cần
            return 0.0; // Thay defaultValue bằng giá trị mặc định bạn muốn sử dụng
        }
    }

    protected Integer getIntegerAndRemove(Map<String, Object> data, String key) {
        Object value = data.get(key);
        data.remove(key);

        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                // Xử lý nếu không thể chuyển đổi thành Integer
                // Ví dụ: có thể trả về giá trị mặc định hoặc thông báo lỗi
                return 0; // Thay defaultValue bằng giá trị mặc định bạn muốn sử dụng
            }
        } else {
            // Xử lý các trường hợp khác nếu cần
            return 0; // Thay defaultValue bằng giá trị mặc định bạn muốn sử dụng
        }
    }
}
