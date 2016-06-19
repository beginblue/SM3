package blue.sm2;


/**
 * Basic convert class for sm2
 * Created by getbl on 2016/6/18.
 */
public class Convert {

//    //zprivate static byte[] hex = new byte[]{};
//
//    private static int charToByte(char c) {
//        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
//        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
//        return (c - '0') & 0x0f;
//    }
//
//    public static long bitStringToLong(String str) {
//        long toRtn = 0;
//        if (str.length() > 64) return 0;
//        else for (int count = str.length() - 1; count > 0; count--) {
//            if (str.charAt(count) == '1') {
//                toRtn += (int) Math.pow(2, count);
//            }
//        }
//        return toRtn;
//    }
//
//    public static String integerToBytes(int integer, int length) {
//        String toRtn = "";
//        while (integer != 0) {
//            int half = integer / 2;
//            int delta = integer - half * 2;
//            toRtn = delta + toRtn;
//            integer /= 2;
//        }
//        if (toRtn.length() < length) {
//            int tempLength = toRtn.length();
//            for (int count = 0;
//                 count < length - tempLength;
//                 count++)
//                toRtn = "0" + toRtn;
//        }
//        return toRtn;
//    }
////
////    public static String Bytes2HexString(byte[] s) {
////
////        byte[] buff = new byte[2 * s.length];
////        for (int i = 0; i < s.length; i++) {
////            buff[2 * i] = hex[(s[i] >> 4) & 0x0f];
////            buff[2 * i + 1] = hex[s[i] & 0x0f];
////        }
////        return new String(buff);
////    }
//
//    public static long hexStringToInteger(String str) {
//        int res = 0;
//        for (int count =0 ; count < str.length();count++){
//            char a = str.charAt(count);
//            if(a>='a'&&a<'g') res+=
//        }
//        return 0;
//    }
//
//
//    public static byte[] HexString2Bytes(String hexstr) {
//        byte[] b = new byte[hexstr.length() / 2];
//        int j = 0;
//        for (int i = 0; i < b.length; i++) {
//            char c0 = hexstr.charAt(j++);
//            char c1 = hexstr.charAt(j++);
//            b[i] = (byte) ((charToByte(c0) << 4) | charToByte(c1));
//        }
//        return b;
//    }


    /**
     * fuck!!!!!!!!!
     * (╯‵□′)╯︵┻━┻
     */

    private static char[] hex = new char[]{'0','1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
}
