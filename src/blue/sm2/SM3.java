package blue.sm2;

import java.util.ArrayList;
import java.util.List;

import static blue.sm2.Convert.*;

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

    public static byte[] boolFFj(int j, byte[] x, byte[] y, byte[] z) {
        if (j >= 0 && j < 16) {
            byte[] temp = xor(x, y);
            return xor(temp, z);
        } else if (j > 15 && j < 64) {
            byte[] xandy = and(x, y);
            byte[] xandz = and(x, z);
            byte[] yandz = and(y, z);
            byte[] or1 = or(xandy, xandz);
            return or(or1, yandz);
        } else return null;
    }

    public static byte[] boolGGj(int j, byte[] x, byte[] y, byte[] z) {
        if (j >= 0 && j < 16) {
            byte[] temp = xor(x, y);
            return xor(temp, z);
        } else if (j > 15 && j < 64) {
            byte[] xandy = and(x, y);
            byte[] xandnotz = and(not(x), z);
            return or(xandy, xandnotz);
        } else return null;
    }

    public static byte[] place0(byte[] x) {
        byte[] temp = xor(x, rotateLeft(x, 9));
        return xor(temp, rotateLeft(x, 17));
    }

    public static byte[] place1(byte[] x) {
//        byte[] temp = xor(x, rotateLeft(x, 15));
//        return xor(temp, rotateLeft(x, 23));
        long xl = byteToInteger(x);
        long x15l = byteToInteger(rotateLeft(x, 15));
        long x23l = byteToInteger(rotateLeft(x, 23));
        return integerToByte(xl ^ x15l ^ x23l);
    }

    /**
     * 自己的padding
     *
     * @param in   消息
     * @param bLen 分组数
     * @return
     */
    public static byte[] padding(byte[] in, int bLen) {

        //计算填充0的长度
        int k = 448 - (8 * in.length + 1) % 512;
        if (k < 0) {
            k = 960 - (8 * in.length + 1) % 512;
        }
        k += 1;
        //生成填充0的bytes
        byte[] padd = new byte[k / 8];
        //0x80=1000 0000  ==> padd = 10000000....
        padd[0] = (byte) 0x80;
        //总长度
        long n = in.length * 8 + bLen * 512;
        //out 是最终要返回的数组
        byte[] out = new byte[in.length + k / 8 + 64 / 8];
        //将消息放进最前面
        System.arraycopy(in, 0, out, 0, in.length);
        //pos是位置索引
        int pos = 0;
        pos += in.length;
        //放入填充padd
        System.arraycopy(padd, 0, out, pos, padd.length);
        //pos指向倒数第八字节
        pos += padd.length;
        //System.out.println("out.length - pos = " + (out.length - pos));
        byte[] tmp = Convert.integerToByte(n, 64);
        //byte[] tmp = back(Convert.integerToByte((int)n,32));
        System.arraycopy(tmp, 0, out, pos, tmp.length);

        //System.out.println(out.length);
        return out;
        //return null;
    }

    public static byte[] CF(byte[] v, byte[] b) {

        if (v.length != 32) return null;
        else {
            byte[][] bytes = new byte[8][4];
        }
        return null;
    }

    public static byte[] expand(byte[] a) {
        //a.length === 512=
        if (a.length != 64) {
            byte[] temp = new byte[64];
            System.arraycopy(a, 0, temp, 64 - a.length, a.length);
            a = temp;
        }
        long[] res = new long[132];
        for (int i = 0; i < 16; i++) {
            byte[] temp = new byte[4];
            temp[0] = a[i * 4];
            temp[1] = a[i * 4 + 1];
            temp[2] = a[i * 4 + 2];
            temp[3] = a[i * 4 + 3];
            res[i] = Convert.byteToInteger(temp);
            System.out.print(Convert.Bytes2HexString(temp) + " ");
        }
        System.out.println("");
        System.out.println("61626380 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000018");
        for (int i = 16; i < 68; i++) {
//            byte[] wj16 = integerToByte(res[i - 16]);
//            byte[] wj13 = integerToByte(res[i - 13]);
//            byte[] wj6 = integerToByte(res[i - 6]);
//            byte[] wj9 = integerToByte(res[i - 9]);
//            byte[] wj3 = integerToByte(res[i - 3]);

            //byte[] w16xor9 = xor(wj16, wj9);
            long w16xor9l = res[i - 16] ^ res[i - 9];
           // byte[] gw16xor9 = Convert.integerToByte(w16xor9l);
//            System.out.println("---");
//            System.out.println(Convert.Bytes2HexString(w16xor9));
//            System.out.println(Convert.Bytes2HexString(gw16xor9));
//            System.out.println("---");
            //byte[] inP1 = xor(w16xor9, rotateLeft(wj3, 15));
            long inP1l = w16xor9l ^ byteToInteger(rotateLeft(Convert.integerToByte(res[i - 3]), 15));
            byte[] ginP1 = integerToByte(inP1l);
            byte[] p1 = place1(ginP1);
            long gp1 = byteToInteger(p1);
            long gp1xor13 = gp1 ^ byteToInteger(rotateLeft(Convert.integerToByte(res[i - 13]), 7));
            //byte[] p1xor13 = xor(p1, rotateLeft(wj13, 7));
            //byte[] over = xor(p1xor13, wj6);
            long overl = gp1xor13 ^ res[i - 6];
            byte[] over = integerToByte(overl);
            res[i] = overl;//byteToInteger(over);
            System.out.print(Convert.Bytes2HexString(over) + " ");
        }
        System.out.println("");
        System.out.println("9092e200 00000000 000c0606 719c70ed 00000000 8001801f 939f7da9 00000000 2c6fa1f9 adaaef14 00000000 0001801e 9a965f89 49710048 23ce86a1 b2d12f1b e1dae338 f8061807 055d68be 86cfd481 1f447d83 d9023dbf 185898e0 e0061807 050df55c cde0104c a5b9c955 a7df0184 6e46cd08 e3babdf8 70caa422 0353af50 a92dbca1 5f33cfd2 e16f6e89 f70fe941 ca5462dc 85a90152 76af6296 c922bdb2 68378cf5 97585344 09008723 86faee74 2ab908b0 4a64bc50 864e6e08 f07e6590 325c8f78 accb8011 e11db9dd b99c0545 ");

        for (int i = 0; i < 64; i++) {
            byte[] ress = xor(integerToByte(res[i]), integerToByte(res[i + 4]));
            res[i + 68] = byteToInteger(ress);
            System.out.print(Convert.Bytes2HexString(ress) + " ");
        }
        System.out.println("");
        System.out.println("61626380 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000018 9092e200 00000000 000c0606 719c70f5 9092e200 8001801f 93937baf 719c70ed 2c6fa1f9 2dab6f0b 939f7da9 0001801e b6f9fe70 e4dbef5c 23ce86a1 b2d0af05 7b4cbcb1 b177184f 2693ee1f 341efb9a fe9e9ebb 210425b8 1d05f05e 66c9cc86 1a4988df 14e22df3 bde151b5 47d91983 6b4b3854 2e5aadb4 d5736d77 a48caed4 c76b71a9 bc89722a 91a5caab f45c4611 6379de7d da9ace80 97c00c1f 3e2d54f3 a263ee29 12f15216 7fafe5b5 4fd853c6 428e8445 dd3cef14 8f4ee92b 76848be4 18e587c8 e6af3c41 6753d7d5 49e260d5 ");

        return null;
    }

    public static byte[] back(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[out.length - i - 1];
        }

        return out;
    }
}