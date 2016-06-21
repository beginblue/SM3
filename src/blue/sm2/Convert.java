package blue.sm2;


import java.util.ArrayList;
import java.util.List;

/**
 * Basic convert class for sm2
 * Created by getbl on 2016/6/18.
 */
public class Convert {


    private static char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static int charToByte(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    public static String Bytes2HexString(byte[] b) {
        char[] buff = new char[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((charToByte(c0) << 4) | charToByte(c1));
        }
        return b;
    }

    public static byte[] integerToByte(long num, int length) {
        byte[] res = new byte[length / 8];
        //TODO:fuck this
        byte[] src = integerToByte(num);
        return or(res, src);
    }

    public static byte[] integerToByte(long num) {
        //7-->00000111
        //255--->11111111
        //256--->00000001 00000000
        //TODO:fuck this
        byte[] bytes = new byte[4];
        //System.out.println();
        int count = bytes.length - 1;
        do {
            try {
                bytes[count--] = (byte) (num & 0xff);
                // System.out.println("tag" + (num & 0xff));

                num >>= 8;

            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
                break;
            }
            //System.out.println("num=" + num);
        } while (num != 0);

        byte[] res = new byte[32 - count];

        return bytes;
    }

    public static long byteToInteger(byte[] b) {
        int num = b.length;
        if (num < 4) {
            byte[] temp = new byte[4];
            System.arraycopy(b, 0, temp, 4 - b.length, b.length);
            b = temp;
        } else if (b.length > 4) return 0;
        long res = 0;
        try {
            res += b[3] & 0xff;
            res += (b[2] & 0xff) << 8;
            res += (b[1] & 0xff) << 16;
            res += (long) (b[0] & 0xff) << 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 循环左移
     *
     * @param sourceByte 待左移动的值
     * @param n          左移动的为数
     * @return
     */
    public static byte rotateLeft(byte sourceByte, int n) {
        // 去除高位的1
        int temp = sourceByte & 0xFF;
        return (byte) ((temp << n) | (temp >>> (8 - n)));
    }

    /**
     * 循环右移
     *
     * @param sourceByte
     * @param n
     * @return
     */
    public static byte rotateRight(byte sourceByte, int n) {
        // 去除高位的1
        int temp = sourceByte & 0xFF;
        return (byte) ((temp >>> n) | (temp << (8 - n)));
    }

    public static String byteToStr(byte b) {
        String str = "";
        for (int i = 7; i >= 0; i--) {
            if ((b >> i & 0x1) == 1) str += "1";
            else if ((b >> i & 0x1) == 0) str += "0";
            else str += "wtf";
        }
        return str;
    }

    /**
     * 循环左移
     *
     * @param sourceBytes
     * @param n
     * @return
     */
    public static byte[] rotateLeft(byte[] sourceBytes, int n) {
        byte[] res = new byte[sourceBytes.length];
        byte removed = 0x0;
        for (int index = sourceBytes.length - 1; index >= 0; --index) {
            int current = sourceBytes[index];
            //System.out.println("source:" + byteToStr((byte) current));
            res[index] = (byte) ((current << n) | removed);
           // System.out.println("current:" + byteToStr(res[index]));
            removed = (byte) ((current & 0xff) >>> (8 - n));
           // System.out.println("removed:" + byteToStr(removed));
        }
        res[sourceBytes.length - 1] |= removed;
        return res;
    }

    public static byte[] rotateRight(byte[] sourceBytes, int n) {
        byte[] out = new byte[sourceBytes.length];
        for (int i = 0; i < sourceBytes.length; i++) {
            out[i] = rotateRight(sourceBytes[i], n);
        }
        return out;
    }

    public static byte[] and(byte[] a, byte[] b) {
        int maxSize = (a.length > b.length) ? a.length : b.length;
        byte[] res = new byte[maxSize];
        for (int count = 1; maxSize - count > 0; ++count) {
            byte tempa = (a.length - count < 0) ? 0 : a[a.length - count];
            byte tempb = (b.length - count < 0) ? 0 : b[b.length - count];
            res[maxSize - count] = (byte) ((tempa & tempb) & 0xff);
        }

        return res;
    }

    public static byte[] or(byte[] a, byte[] b) {
        int maxSize = (a.length > b.length) ? a.length : b.length;
        byte[] res = new byte[maxSize];
        for (int count = 1; maxSize - count > 0; ++count) {
            byte tempa = (a.length - count < 0) ? 0 : a[a.length - count];
            byte tempb = (b.length - count < 0) ? 0 : b[b.length - count];
            res[maxSize - count] = (byte) ((tempa | tempb) & 0xff);
        }

        return res;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        int maxSize = (a.length > b.length) ? a.length : b.length;
        byte[] res = new byte[maxSize];
        for (int count = 1; maxSize - count > 0; ++count) {
            byte tempa = (a.length - count < 0) ? 0 : a[a.length - count];
            byte tempb = (b.length - count < 0) ? 0 : b[b.length - count];
            res[maxSize - count] = (byte) ((tempa ^ tempb) & 0xff);
        }

        return res;
    }

    public static byte[] not(byte[] a) {
        byte[] c = new byte[a.length];
        for (int cout = 0; cout < a.length; cout++) {
            c[cout] = (byte) ~a[cout];
        }
        return c;
    }

    public static byte[] mod32add(byte[] a, byte[] b) {
        int maxSize = (a.length > b.length) ? a.length : b.length;
        byte inProgress = 0;
        byte[] res = new byte[maxSize];
        for (int count = 1; maxSize - count > 0; ++count) {
            byte tempa = (a.length - count < 0) ? 0 : a[a.length - count];
            byte tempb = (b.length - count < 0) ? 0 : b[b.length - count];
            res[maxSize - count] = (byte) ((tempa + tempb + inProgress) & 0xff);
            inProgress = (byte) (((tempa + tempb + inProgress) >> 8) & 0xff);
        }

        return res;
    }


}
