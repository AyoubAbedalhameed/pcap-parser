package org.example;

import org.example.handlers.testHandler;
import io.pkts.Pcap;


public class Main {


    public static void main(String[] args) throws Exception {

        Request request = new Request();
        request.setpCount(2);

        Pcap pcap = Pcap.openStream("/home/ayoub/Desktop/JAVA/pkts-io-test/src/main/resources/test.pcap");
        pcap.loop(new testHandler(request));
        pcap.close();


    }



}