import java.util.BitSet;

public class Main {
    public static void main(String[] args) {
//        int a = 10;
//        int a1 = a << 1;
//        BitSet bitSet = new BitSet(64);
//        System.out.println(bitSet.size());
//        System.out.println(bitSet.get(63));

        int a = 2;
        int b = -2;
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(a>>1));
        System.out.println(Integer.toBinaryString(a>>>1));
        System.out.println("----------------");
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(b>>1));
        System.out.println(Integer.toBinaryString(b>>>1));

//        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3"));
//        String s = JSON.toJSONString(l, SerializerFeature.WRITE_MAP_NULL_FEATURES);
//        System.out.println("11111111: " + s);
    }
}
