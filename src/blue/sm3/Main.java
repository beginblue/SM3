package blue.sm3;


import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        byte[] bytes = Convert.HexString2Bytes("61626364616263646162636461626364616263646162636461626364616263646162636461626364616263646162636461626364616263646162636461626364");

//        File file = new File("C:\\Users\\getbl\\Desktop\\SM2\\test.txt");
//        InputStream inputStream = new FileInputStream(file);
//        long length = file.length();
//        byte[] bytes = new byte[(int) length];
//        inputStream.read(bytes);
        String str = SM3.hash(bytes);
        System.out.println("");
        System.out.println(str);

    }

}
