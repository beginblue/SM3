package blue.sm2;


public class Main {

    public static void main(String[] args) {
        byte[] bytes = Convert.HexString2Bytes("616263");//4024364767
        //byte[] bytes = Convert.integerToByte(24845);//241+3975
        // String str = Convert.Bytes2HexString(bytes);
        //   x System.out.println(str);
        //   System.out.println(Convert.byteToInteger(bytes));
        byte[] pad = SM3.padding(bytes, 0);
        System.out.println(Convert.Bytes2HexString(pad) + " length = " + pad.length * 8);
        SM3.expand(pad);

//        byte[] bytes = Convert.HexString2Bytes("d3edc2");//‭110100111110110111000010
//
//        byte[] bytes1 = Convert.rotateLeft(bytes,3);        //‭100111110110111000010110‬
//        System.out.println(Convert.Bytes2HexString(bytes1));
//        System.out.println(Convert.byteToInteger(bytes1));//‭100101100001100100010‬101



    }

}
