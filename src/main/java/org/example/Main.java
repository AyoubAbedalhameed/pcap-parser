package org.example;

import org.example.handlers.testHandler;
import io.pkts.Pcap;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {


    public static void main(String[] args){

        Request request = new Request();
        request.setpCount(2);
        String dFile = "/home/ayoub/Desktop/JAVA/pcap-decoder/pcap-decoder/src/main/resources/test.pcap";




        try {
            Pcap pcap = Pcap.openStream(dFile);
            pcap.loop(new testHandler(request));
            pcap.close();
        } catch (FileNotFoundException e){
            System.out.println("Destination file not found");
            System.exit(-1);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }




    }



}