import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3"));
        String s = JSON.toJSONString(l, SerializerFeature.WRITE_MAP_NULL_FEATURES);
        System.out.println("11111111: " + s);
    }
}
