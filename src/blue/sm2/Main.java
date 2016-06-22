package blue.sm2;


public class Main {

    public static void main(String[] args) {
        byte[] bytes = Convert.HexString2Bytes("61626364616263646162636461626364616263646162636461626364616263646162636461626364616263646162636461626364616263646162636461626364");
//byte[] bytes = Convert.HexString2Bytes("616263");
//        byte[] bytes1 = SM3.place1(bytes);
//        System.out.println(Convert.bytesToStr(bytes1));
//        byte[] padding = SM3.padding(bytes, 0);
        //long[] expand = SM3.expand(padding);
        //SM3.CF(SM3.IV,expand);
         String str = SM3.hash(bytes);
        System.out.println("");
        System.out.println(str);

    }

}
