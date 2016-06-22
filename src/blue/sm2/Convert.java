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
        } while (num != 0 && count >= 0);

        //byte[] res = new byte[32 - count];

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

    public static String byteToStr(byte b) {
        String str = "";
        for (int i = 7; i >= 0; i--) {
            if ((b >> i & 0x1) == 1) str += "1";
            else if ((b >> i & 0x1) == 0) str += "0";
            else str += "wtf";
        }
        return str;
    }

    public static String bytesToStr(byte[] bytes) {
        String str = "";
        for (byte b :
                bytes) {
            str += byteToStr(b);
        }
        return str;
    }

    public static byte[] strToBytes(String string) {


        int count = string.length() / 8;
        byte[] res = new byte[count];
        //System.out.println(string.length());
        // for (int i = count-1; i >= 0; --i) {
        for (int i = 0; i < count; i++) {
            String substring;
            int check = string.length() - (i + 1) * 8;
            if (check >= 0) {
                substring = string.substring(check, check + 8);
                //  System.out.println(substring + "-" + i + "-" + check);
            } else substring = "";
            // System.out.println(substring.length());
            for (int j = 7; j >= 0; j--) {
                if (substring.charAt(j) == '1') res[count - i - 1] += 1 << (8 - j - 1);
            }
            ///res[i] = Byte.valueOf(substring);
            //String substring = string.substring(string.length() - 8 * (count + 1) < 0 ? 0 : string.length() - (count + 1) * 8, 8);

        }

        return res;
    }

    public static byte[] rotateLeft(byte[] sourceBytes, int n) {
        String s = bytesToStr(sourceBytes);
        while(n>=s.length()) n-=s.length();
        String subhead = s.substring(0, n);
        String subbody = s.substring(n);
        return strToBytes(subbody + subhead);

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

    public static long[] bytesToIntegers(byte[] a) {
        if (a.length != 64) {
            byte[] temp = new byte[64];
            System.arraycopy(a, 0, temp, 64 - a.length, a.length);
            a = temp;
        }
        long[] res = new long[64];
        for (int i = 0; i < 16; i++) {
            byte[] temp = new byte[4];
            temp[0] = a[i * 4];
            temp[1] = a[i * 4 + 1];
            temp[2] = a[i * 4 + 2];
            temp[3] = a[i * 4 + 3];
            res[i] = Convert.byteToInteger(temp);
            System.out.print(Convert.Bytes2HexString(temp) + " ");
        }

        return res;
    }


    public static long rotateLeft(long numver, int count) {
        return byteToInteger(Convert.rotateLeft(integerToByte(numver), count));
    }
}
