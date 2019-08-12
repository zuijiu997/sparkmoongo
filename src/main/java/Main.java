import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       int a = 10;
        int a1 = a<<1;
        BitSet bitSet = new BitSet();
        System.out.println(bitSet.size());
        System.out.println(bitSet.get(63));

//        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3"));
//        String s = JSON.toJSONString(l, SerializerFeature.WRITE_MAP_NULL_FEATURES);
//        System.out.println("11111111: " + s);
    }
}
