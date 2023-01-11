package org.example.helpers;

import org.example.Request;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Printer{
    private static void print(Request request, PrintStream printStream, String line, boolean head){
        if(head) printStream.print("\n\n#" + request.getpCount()); else
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

}