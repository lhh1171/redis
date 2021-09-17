package Rd;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

public class Server {
    static final boolean RUNNING =true;

    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(6379);
        while (RUNNING ){
           Socket s= ss.accept();
            OutputStream outputStream=s.getOutputStream();
            InputStream inputStream=s.getInputStream();
            HashMap<String,String> map=new HashMap<>();
            int len=0;
            int com=0;
            String getname="";
            String mm="";
            String setk="";
            String setv="";
            while (true){
                com++;
                String d="";
                while((len=inputStream.read())!=10){
                    d+=(char)len;
                }
                if (com==3){
                    mm=d;
                }
                if (com==5){
                    setk= d.replace("\r","");
                    getname=d.replace("\r","");
                }
                if (com==7){
                    setv= d.replace("\r","");
                }
                System.out.println(com);
                System.out.println(d);

                if (mm.equals("COMMAND\r")){
                        if (com==3){
                            outputStream.write("+ok\r\n".getBytes());
                            com=0;
                        }
                    }
                if (mm.equals("get\r")){
                    if (com==5){

                        outputStream.write(("$1\r\n"+map.get(getname)+"\r\n").getBytes());
                        com=0;
                    }
                }
                if (mm.equals("set\r")){
                    if (com==7){
                        map.put(setk,setv);
                        outputStream.write("+ok\r\n".getBytes());
                        com=0;
                    }
                }
            }

        }
    }
    
}

