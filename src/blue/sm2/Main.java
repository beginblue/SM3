package blue.sm2;


public class Main {

    public static void main(String[] args){

        byte[] a = Convert.HexString2Bytes("3f4d");
        byte[] b = Convert.HexString2Bytes("9e4c");
        byte[] c = SM3.and(a,b);
        String str = Convert.Bytes2HexString(c);
        System.out.println(str);
    }

}
