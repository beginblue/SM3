package blue.sm3;

import java.util.ArrayList;
import java.util.List;

import static blue.sm3.Convert.*;

/**
 * sm3 hash
 * Created by getbl on 2016/6/19.
 */
public class SM3 {
    static {
        IV = HexString2Bytes("7380166f4914b2b9172442d7da8a0600a96f30bc163138aae38dee4db0fb0e4e");
    }

    private static byte[] IV;

    private static byte[] getTj(int j) {
        if (j >= 0 && j < 16) return HexString2Bytes("79cc4519");
        else if (j > 15 && j < 64) return HexString2Bytes("7a879d8a");
        else return null;
    }

    private static byte[] boolFFj(int j, byte[] x, byte[] y, byte[] z) {
        long xl, yl, zl;
        xl = byteToInteger(x);
        yl = byteToInteger(y);
        zl = byteToInteger(z);

        if (j >= 0 && j < 16) {
            //byte[] temp = xor(x, y);
//            return xor(temp, z);
            return integerToByte(xl ^ yl ^ zl);
        } else if (j > 15 && j < 64) {
//            byte[] xandy = and(x, y);
//            byte[] xandnotz = and(not(x), z);
//            return or(xandy, xandnotz);
            return integerToByte((xl & yl) | (xl & zl) | (yl & zl));
        } else return null;
    }

    private static byte[] boolGGj(int j, byte[] x, byte[] y, byte[] z) {
        long xl, yl, zl;
        xl = byteToInteger(x);
        yl = byteToInteger(y);
        zl = byteToInteger(z);

        if (j >= 0 && j < 16) {
            return integerToByte(xl ^ yl ^ zl);
        } else if (j > 15 && j < 64) {
            return integerToByte((xl & yl) | (~xl & zl));
        } else return null;
    }

    private static long place0(long x) {
        return x ^ rotateLeft(x, 9) ^ rotateLeft(x, 17);
    }

    private static byte[] place1(byte[] x) {
        long xl = byteToInteger(x);
        long x15l = byteToInteger(rotateLeft(x, 15));
        long x23l = byteToInteger(rotateLeft(x, 23));
        return integerToByte(xl ^ x15l ^ x23l);
    }

    private static byte[] padding(byte[] in, int bLen) {
        System.out.println("padding");
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
        byte[] tmp = integerToByte(n, 64);
        //byte[] tmp = back(integerToByte((int)n,32));
        System.arraycopy(tmp, 0, out, pos, tmp.length);

        System.out.println(Bytes2HexString(out));
        //System.out.println(out.length);
        return out;
        //return null;
    }

    private static byte[] CF(byte[] v, long[] b) {
        System.out.println("CF");
        System.out.println("");
        long[] words = new long[8];
        long[] base = new long[8];
        for (int i = 0; i < 8; i++) {
            byte[] temp = new byte[4];
            temp[0] = v[i * 4];
            temp[1] = v[i * 4 + 1];
            temp[2] = v[i * 4 + 2];
            temp[3] = v[i * 4 + 3];
            words[i] = byteToInteger(temp);
            String str = Bytes2HexString(integerToByte(words[i]));
            base[i] = words[i];
            System.out.print(str + " ");
        }
        System.out.println("");
        long ss1, ss2, tt1, tt2;
        for (int i = 0; i < 64; i++) {
            ss1 = rotateLeft(
                    rotateLeft(words[0], 12)
                            + words[4]
                            +
                            rotateLeft(
                                    byteToInteger(getTj(i)),
                                    i
                            )
                    , 7);

            ss2 = rotateLeft(words[0], 12) ^ ss1;
            tt1 = byteToInteger(
                    boolFFj(i,
                            integerToByte(words[0]),
                            integerToByte(words[1]),
                            integerToByte(words[2])))
                    + words[3] + ss2 + b[68 + i];
            tt2 = byteToInteger(
                    boolGGj(i,
                            integerToByte(words[4]),
                            integerToByte(words[5]),
                            integerToByte(words[6]))
            ) + words[7] + ss1 + b[i];

            words[3] = words[2];
            words[2] = rotateLeft(words[1], 9);
            words[1] = words[0];
            words[0] = tt1;
            words[7] = words[6];
            words[6] = rotateLeft(words[5], 19);
            words[5] = words[4];
            words[4] = place0(tt2);

            System.out.print(i + ":");
            for (int j = 0; j < 8; j++) {
                String str = Bytes2HexString(integerToByte(words[j]));

                System.out.print(str + " ");
            }
            System.out.println("");
        }
        String hexStr = "";

        System.out.println("");
        for (int j = 0; j < 8; j++) {
            String str = Bytes2HexString(integerToByte(words[j] ^ base[j]));
            System.out.print(str + " ");
            hexStr += str;
        }


        return HexString2Bytes(hexStr);
    }

    private static long[] expand(byte[] a) {
        System.out.println("expand");
        //a.length === 512=
        if (a.length < 64) {
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
            res[i] = byteToInteger(temp);
            System.out.print(Bytes2HexString(temp) + " ");
        }
        System.out.println("");
        //System.out.println("61626380 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000018");
        for (int i = 16; i < 68; i++) {
            long wj16l = res[i - 16]; //byte[] wj16 = integerToByte(res[i - 16]);
            long wj13l = res[i - 13]; //byte[] wj13 = integerToByte(res[i - 13]);
            long wj6l = res[i - 6]; //byte[] wj6 = integerToByte(res[i - 6]);
            long wj9l = res[i - 9];// byte[] wj9 = integerToByte(res[i - 9]);
            long wj3l = res[i - 3]; // byte[] wj3 = integerToByte(res[i - 3]);

            long w16xor9l = wj16l ^ wj9l;//byte[] w16xor9 = xor(wj16, wj9);
            byte[] b = rotateLeft(integerToByte(wj3l), 15);
            String str = bytesToStr(b);
            long inP1l = w16xor9l ^ byteToInteger(b);//byte[] inP1 = xor(w16xor9, rotateLeft(wj3, 15));
            byte[] p1 = place1(integerToByte(inP1l));//byte[] p1 = place1(inP1);
            String _str = bytesToStr(p1);
            byte[] b1 = rotateLeft(integerToByte(wj13l), 7);
            String __str = bytesToStr(b1);
            long p1xor13l = byteToInteger(p1) ^ byteToInteger(b1);//byte[] p1xor13 = xor(p1, rotateLeft(wj13, 7));
            long overl = p1xor13l ^ wj6l;//byte[] over = xor(p1xor13, wj6);
            res[i] = overl;//res[i] = byteToInteger(over);
            System.out.print(Bytes2HexString(integerToByte(overl)) + " ");
        }
        System.out.println("");
        //System.out.println("9092e200 00000000 000c0606 719c70ed 00000000 8001801f 939f7da9 00000000 2c6fa1f9 adaaef14 00000000 0001801e 9a965f89 49710048 23ce86a1 b2d12f1b e1dae338 f8061807 055d68be 86cfd481 1f447d83 d9023dbf 185898e0 e0061807 050df55c cde0104c a5b9c955 a7df0184 6e46cd08 e3babdf8 70caa422 0353af50 a92dbca1 5f33cfd2 e16f6e89 f70fe941 ca5462dc 85a90152 76af6296 c922bdb2 68378cf5 97585344 09008723 86faee74 2ab908b0 4a64bc50 864e6e08 f07e6590 325c8f78 accb8011 e11db9dd b99c0545 ");

        for (int i = 0; i < 64; i++) {
            //byte[] ress = xor(integerToByte(res[i]), integerToByte(res[i + 4]));
            res[i + 68] = res[i] ^ res[i + 4];
            //res[i + 68] = byteToInteger(ress);
            System.out.print(Bytes2HexString(integerToByte(res[i + 68])) + " ");
        }
        System.out.println("");
        //System.out.println("61626380 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000018 9092e200 00000000 000c0606 719c70f5 9092e200 8001801f 93937baf 719c70ed 2c6fa1f9 2dab6f0b 939f7da9 0001801e b6f9fe70 e4dbef5c 23ce86a1 b2d0af05 7b4cbcb1 b177184f 2693ee1f 341efb9a fe9e9ebb 210425b8 1d05f05e 66c9cc86 1a4988df 14e22df3 bde151b5 47d91983 6b4b3854 2e5aadb4 d5736d77 a48caed4 c76b71a9 bc89722a 91a5caab f45c4611 6379de7d da9ace80 97c00c1f 3e2d54f3 a263ee29 12f15216 7fafe5b5 4fd853c6 428e8445 dd3cef14 8f4ee92b 76848be4 18e587c8 e6af3c41 6753d7d5 49e260d5 ");

        return res;
    }

    static String hash(byte[] input) {
        System.out.println("hash");
        int part = input.length / 64 + 1;
        byte[][] pool = new byte[part][];
        List<byte[]> bytes = new ArrayList<>();

        for (int i = 0; i < part; i++) {
            //pool[i]=input
//            System.arraycopy(input, i * 64, pool[i], 0, input.length - i * 64 > 64 ? 64 : input.length - i * 64);
            int count = input.length - i * 64;
            byte[] temp;
            if (count > 64) {
                temp = new byte[64];
                System.arraycopy(input, i * 64, temp, 0, 64);
                bytes.add(temp);
            } else {
                temp = new byte[count];
                System.arraycopy(input, i * 64, temp, 0, count);
                bytes.add(temp);
            }
        }
        pool = bytes.toArray(pool);
        System.out.println(Bytes2HexString(pool[part - 1]));
        pool[part - 1] = padding(pool[part - 1], part-1);

        byte[] v = IV;

        for (int i = 0; i < part; i++) {
            v = CF(v, expand(pool[i]));
        }

        return Bytes2HexString(v);

    }


}
