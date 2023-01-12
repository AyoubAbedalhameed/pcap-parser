package org.example.helpers;

import io.pkts.packet.IPv4Packet;
import org.example.Request;
import org.example.packets.AvpPacket;
import org.example.packets.DiameterPacket;

import java.io.*;

public class Printer{
    private static void print(Request request, PrintStream printStream, String line, boolean head){
        if(head) printStream.print("\n\n#" + request.getPacketCounter() + " "); else
            printStream.print("       " + request.getPacketCounter() + "." + request.getAvpCounter() +" --> ");

        printStream.println(line);

    }


    public static void print(Request request, String line, boolean head) throws FileNotFoundException{
        if(request.getDesFile() == null){
            print(request, System.out, line, head);
        }
        else{
            PrintStream printStream = new PrintStream(new FileOutputStream(request.getDesFile(), true));
            print(request, printStream, line, head);
            printStream.close();
        }
    }

    public static void print(Request request, IPv4Packet iPv4Packet, DiameterPacket diameterPacket) throws FileNotFoundException, IOException {
       String line = diameterPacket.commandName() + "     SIP: " + iPv4Packet.getSourceIP() + "    DIP: " + iPv4Packet.getDestinationIP();
        if(request.getDesFile() == null){
            print(request, System.out,  line, true);
        }
        else{
            PrintStream printStream = new PrintStream(new FileOutputStream(request.getDesFile(), true));
            print(request, printStream,  line, true);
            printStream.close();
        }
    }


    public static void print(Request request, AvpPacket avpPacket) throws FileNotFoundException, IOException {
        String attributeName = avpPacket.getAttributeName();
        if(attributeName == null) attributeName = "Unknown-";
        String data;

        if(attributeName.charAt(attributeName.length()-1) == '#')
            data = avpPacket.getData(true);
        else data = avpPacket.getData(false);

        StringBuilder att = new StringBuilder(attributeName);
        att.setLength(att.length() -1);
        attributeName = att.toString();
        String line = attributeName + ":   " + data;

        if(request.getDesFile() == null){
            print(request, System.out,  line, false);
        }
        else{
            PrintStream printStream = new PrintStream(new FileOutputStream(request.getDesFile(), true));
            print(request, printStream,  line, false);
            printStream.close();
        }
    }
}