package org.example.helpers;

import io.pkts.packet.IPv4Packet;
import org.example.Request;
import org.example.packets.AvpPacket;
import org.example.packets.DiameterPacket;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Printer{
    private static void print(Request request, PrintStream printStream, String line, boolean head){
        if(head) printStream.print("\n\n#" + request.getPacketCounter() + " "); else
            printStream.print("       " + request.getPacketCounter() + "." + request.getAvpCounter() +" -->");

        printStream.println(line);

    }


    public static void print(Request request, String line, boolean head) throws FileNotFoundException{
        if(request.getDesFile() == null){
            print(request, System.out, line, head);
        }
        else{
            PrintStream printStream = new PrintStream(request.getDesFile());
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
            PrintStream printStream = new PrintStream(request.getDesFile());
            print(request, printStream,  line, true);
            printStream.close();
        }
    }




    public static void print(Request request, AvpPacket avpPacket) throws FileNotFoundException, IOException {

        String line = avpPacket.getAttributeName() + ":   " + avpPacket.getData();

        if(request.getDesFile() == null){
            print(request, System.out,  line, false);
        }
        else{
            PrintStream printStream = new PrintStream(request.getDesFile());
            print(request, printStream,  line, false);
            printStream.close();
        }
    }
}