package blue.sm2;

import java.util.ArrayList;
import java.util.List;

/**
 * sm3 hash
 * Created by getbl on 2016/6/19.
 */
public class SM3 {
//    //long iv = 0x7380166f4914b2b9172442d7da8a0600a96f30bc163138aae38dee4db0fb0e4e;
//
//    private static int T;
//
//
//    private int getT(int j) {
//        if (j >= 0 && j <= 15) return 0x79cc5419;
//        else if (j > 15 && j < 64) return 0x7a879d8a;
//        else return 0;
//    }
//
//    public int boolFunctionF(int j, int x, int y, int z) {
//        if (j >= 0 && j < 16) return x ^ y ^ z;
//        else if (j > 15 && j < 64) return (x & y) | (x & z) | (y & z);
//        else return 0;
//    }
//
//    public int boolFunctionG(int j, int x, int y, int z) {
//        if (j >= 0 && j < 16) return x ^ y ^ z;
//        else if (j > 15 && j < 64) return (x & y) | ((~x) & z);
//        else return 0;
//    }
//
//    public int place0(int x) {
//        return x | (x << 9) | (x << 17);
//    }
//
//    public int place1(int x) {
//        return x | (x << 15) | (x << 23);
//    }
//
//    public String  padding (int x){
//        String message = Convert.integerToBytes(x,0);
//        String msgLeng = Convert.integerToBytes(message.length(),64);
//        message+="1";
//        int time = (int) (message.length())/512;
//        int numberOfZeros = (512*(time)-message.length()+448);
//        for(int count  =0 ;count<numberOfZeros;count++){
//            message+="0";
//        }
//        message+=msgLeng;
//        System.out.println(message.length());
//
//        return  message;
//    }

    static {
        IV = Convert.HexString2Bytes("7380166f 4914b2b9 172442d7 da8a0600 a96f30bc 163138aa e38dee4d b0fb0e4e");
    }

    private static byte[] IV;

    private static byte[] Tj;

    private static byte[] getTj(int j) {
        if (j >= 0 && j < 16) return Convert.HexString2Bytes("79cc4519");
        else if (j > 15 && j < 64) return Convert.HexString2Bytes("7a879d8a");
        else return null;
    }

    /**
     * 循环左移
     * @param sourceByte 待左移动的值
     * @param n 左移动的为数
     * @return
     */
    public static byte rotateLeft(byte sourceByte, int n) {
        // 去除高位的1
        int temp = sourceByte & 0xFF;
        return (byte) ((temp << n) | (temp >>> (8 - n)));
    }
    /**
     * 循环右移
     * @param sourceByte
     * @param n
     * @return
     */
    public static byte rotateRight(byte sourceByte, int n) {
        // 去除高位的1
        int temp = sourceByte & 0xFF;
        return (byte) ((temp >>> n) | (temp << (8 - n)));
    }
    /**
     * 循环左移
     * @param sourceBytes
     * @param n
     * @return
     */
    public static byte[] rotateLeft(byte[] sourceBytes, int n) {
        byte[] out = new byte[sourceBytes.length];
        for (int i = 0; i < sourceBytes.length; i++) {
            out[i] = rotateLeft(sourceBytes[i], n);
        }
        return out;
    }

    public static byte[] rotateRight(byte[] sourceBytes, int n) {
        byte[] out = new byte[sourceBytes.length];
        for (int i = 0; i < sourceBytes.length; i++) {
            out[i] = rotateRight(sourceBytes[i], n);
        }
        return out;
    }




}