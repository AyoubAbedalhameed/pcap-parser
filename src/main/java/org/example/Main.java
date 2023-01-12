package org.example;

import org.apache.commons.cli.*;
import org.example.handlers.GlobalHandler;
import io.pkts.Pcap;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    //Options object considered as a container/registry for all available options
    private static Options options;
    static {
        options = new Options();


        //Define an apache.common.cli Option object, then add it to the Options registry.
        Option sourceIP = Option.builder("sip").longOpt("src-ip")
                .hasArg(true)
                .argName("sip")
                .desc("Filter packets according to the given source IP").build();
        options.addOption(sourceIP);



        Option destinationIP = Option.builder("dip").longOpt("dest-ip")
                .hasArg(true)
                .argName("sip")
                .desc("Filter packets according to the given destination IP").build();
        options.addOption(destinationIP);



        Option srcPort = Option.builder("sp").longOpt("src-port")
                .hasArg(true)
                .argName("sp")
                .desc("Filter packets according to the given source port").build();
        options.addOption(srcPort);




        Option dstPort = Option.builder("dp").longOpt("dest-port")
                .hasArg(true)
                .argName("dp")
                .desc("Filter packets according to the given destination port").build();
        options.addOption(dstPort);



        Option commandCode = Option.builder("dcc").longOpt("command-code")
                .hasArg(true)
                .argName("dcc")
                .desc("Filter packets according to a specific diameter command").build();
        options.addOption(commandCode);


        Option avpCode = Option.builder("avp").longOpt("attribute-value-code")
                .hasArg(true)
                .argName("attribute-value-id")
                .desc("Filter packets according to the specified AVP number").build();
        options.addOption(avpCode);


        Option maxPCount = Option.builder("mc").longOpt("max-count")
                .hasArg(true)
                .argName("mc")
                .desc("decode messages up to the given max-count, default is 50").build();
        options.addOption(maxPCount);


        Option avpAtt = Option.builder("mc").longOpt("max-count")
                .hasArg(true)
                .argName("mc")
                .desc("decode messages up to the given max-count").build();
        options.addOption(avpAtt);


        Option redirectOutput = Option.builder("r").longOpt("redirect")
                .hasArg(true)
                .argName("file")
                .desc("Redirect the output from the stdout to the given file").build();
        options.addOption(redirectOutput);


        Option pcapFile = Option.builder("pcap").longOpt("pcap")
                .hasArg(true)
                .required()
                .argName("sFile")
                .desc("source packets capture file").build();
        options.addOption(pcapFile);



        //All the following are FLAGS options, then do not have values.
        //verbose mode option is added but not implemented yet, adding this option in the command line has no effect on the output.
        options.addOption("qo", "requests-only", false, "Diameter requests messages only");
        options.addOption("so", "response-only", false, "Diameter response messages only");
        options.addOption("v", "verbose", false, "verbose output");

    }


    public static void main(String[] args){

        //apache.common.cli CommandLineParser interface defines the general parsing contract by one method only CommandLine parse(..)
        CommandLineParser parser = new DefaultParser();

        //Help formatter generate a POSIX like usage message automatically based on the available options.
        HelpFormatter helpFormatter = new HelpFormatter();

        /*apache.common.cli CommandLine is nothing but an object that is produced by the Parser that holds the actual well-formatted options provided by the user
          by maintaining two String lists, one for the given options and the other for their args. */
        CommandLine cmd = null;

        //Create a new request object for each new request if the program is running in the iterative mode.
        Request request = new Request();

        try{
            cmd = parser.parse(options, args);

            /*The following is just simple validation of the options' combination, we can use the API for that but the
            * following is the only bad combination of the current available options*/
            if(cmd.hasOption("qo") && cmd.hasOption("so")) throw new ParseException("Wrong options combination");



            //Setting the request parameters.
            request.setRespOnly(cmd.hasOption("so"));
            request.setReqOnl(cmd.hasOption("qo"));
            request.setVerbose(cmd.hasOption("v"));

            if (cmd.hasOption("sip"))
                request.setSIP(cmd.getOptionValue("sip"));

            if (cmd.hasOption("dip"))
                request.setDIP(cmd.getOptionValue("dip"));

            if (cmd.hasOption("sp"))
                request.setSctpSPort(Integer.parseInt(cmd.getOptionValue("sp")));

            if (cmd.hasOption("dp"))
                request.setSctpDPort(Integer.parseInt(cmd.getOptionValue("dp")));

            if (cmd.hasOption("dcc"))
                request.setDiameterCommandCode(Integer.parseInt(cmd.getOptionValue("dcc")));

            if (cmd.hasOption("avp"))
                request.setAvpCode(Integer.parseInt(cmd.getOptionValue("avp")));

            if (cmd.hasOption("mc"))
                request.setpCount(Integer.parseInt(cmd.getOptionValue("mc")));

            if (cmd.hasOption("r"))
                request.setDesFile(cmd.getOptionValue("r"));


        } catch (ParseException e){
            System.out.println(e.getMessage());
            helpFormatter.printHelp(" " ,options);
            System.exit(-1);
        }

        /* Here I used pkts.io which is a native lib for parsing pcap, considered as an alternatives for other java (non-native)
        * c-pcap-lib wrappers such as parse4j, jparser, jnetparser, etc.
        * for more details about pkts.io visit:  https://github.com/aboutsip/pkts*/
        try {
            Pcap pcap = Pcap.openStream(cmd.getOptionValue("pcap"));
            pcap.loop(new GlobalHandler(request));

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