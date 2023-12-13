package OOP.BE.datascraping.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoaderHandlers {
    public static String getStringAndRemove(Map<String, Object> data, String key) {
        String value = (String) data.get(key);
        data.remove(key);
        return value;
    }

    public static Double getDoubleAndRemove(Map<String, Object> data, String key) {
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

    public static Integer getIntegerAndRemove(Map<String, Object> data, String key) {
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

    public static List<String> getListAndRemove(Map<String, Object> data, String key) {
        String value = data.get(key).toString();
        List<String> tags = Arrays.asList(value.substring(1, value.length() - 1).split(", "));
        data.remove(key);
        return tags;
    }
}
