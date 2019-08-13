import java.util.BitSet;

/**
 * 有符号的数字二进制表达是用补码表示的，正数的补码和原码相同，负数的源码是反码加1，求反码是符号位不变
 *
 * >> << 符号位不动
 * >>>符号位动
 */

public class BitSetUtils {
    public static BitSet convert(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }
}
