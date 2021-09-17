package Rd;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static boolean running=true;
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket s=new Socket("127.0.0.1",6379);
        OutputStream outputStream=s.getOutputStream();
        InputStream inputStream=s.getInputStream();
        DataInputStream dis=new DataInputStream(inputStream);
        DataOutputStream dos=new DataOutputStream(outputStream);
 //       Scanner sc=new Scanner(System.in);
//        while (true){
//            while (sc.hasNext()){
//                String sf=sc.next();
//                String[] sff=sf.split(" ");
//                int[] sfff=new int[sff.length];
//                for (int i = 0; i < sff.length; i++) {
//                        sfff[i]=sff[i].toCharArray().length;
//                    }
//                StringBuilder sss= new StringBuilder("*" + sff.length + "\r\n");
//                for (int i = 0; i < sff.length; i++) {
//                    sss.append("$").append(sfff[i]).append(sff[i]).append("\r\n");
//                }
//                dos.writeUTF(sss.toString());
//            }
//        }
        outputStream.write("*3\r\n$3\r\nset\r\n$3\r\nkkk\r\n$1\r\n3\r\n".getBytes());
       // outputStream.write("*2\r\n$3\r\nget\r\n$3\r\nkkk\r\n".getBytes());
        while (true){
            System.out.println(inputStream.read());
        }
    }
}
