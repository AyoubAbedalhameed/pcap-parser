package org.example.packets;

import io.pkts.buffer.Buffer;
import org.example.helpers.PktBuffWrapper;
import org.example.protocols.DiameterPFields;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiameterPacket {
    public final Buffer message;
    private final PktBuffWrapper bufferWrapper ;







    public DiameterPacket(Buffer message){
        this.message = message;
        bufferWrapper = PktBuffWrapper.getInstance();
        message.setReaderIndex(20);

    }

    public int version() throws IndexOutOfBoundsException{

        return message.getUnsignedByte(DiameterPFields.VERSION.getIndex());
    }

    public int messageLength() throws IOException{
        return (int) bufferWrapper.getInt(message, DiameterPFields.MESSAGE_LENGTH);
    }

    public boolean isRequest() throws IOException {
        return bufferWrapper.getBool(message, DiameterPFields.MESSAGE_TYPE);
    }

    public boolean isProxiable() throws IOException{
        return bufferWrapper.getBool(message, DiameterPFields.PROXIABLE);
    }

    public boolean errorFlag() throws IOException{
        return bufferWrapper.getBool(message, DiameterPFields.ERROR);
    }

    public boolean isRetransmitted() throws IOException{
        return bufferWrapper.getBool(message, DiameterPFields.RETRANSMITTED);
    }


    public long applicationID() throws IOException{
        return bufferWrapper.getInt(message, DiameterPFields.APPLICATION_ID);
    }


    public long hHID() throws IOException{
        return bufferWrapper.getInt(message, DiameterPFields.HHID);
    }


    public long eEID() throws IOException{
        return bufferWrapper.getInt(message, DiameterPFields.EEID);
    }

    public long commandCode() throws IOException{
        return bufferWrapper.getInt(message, DiameterPFields.COMMAND_CODE);
    }

    public String commandName() throws IOException{
        String localCommandName = commandCodes.get( Integer.valueOf( (int) commandCode()) );
        if(isRequest()) return localCommandName + "request";
        return localCommandName + "response";
    }

    public void resetAVPIndex(){
        message.setReaderIndex(20);
    }

    public AvpPacket nextAVP() throws IOException{
        long nextAVPLength = bufferWrapper.getInt(message, message.getReaderIndex() + 5,3);
        int padding = (int) (4 - (nextAVPLength % 4));
        if(padding == 4) padding =0;
        return new AvpPacket(message.readBytes((int) nextAVPLength + padding));
    }

    //TODO: Here you should implement an initializer, it should runs @ the beginning of the program.
    public static Map<Object, String>  commandCodes = null;
    static {
        commandCodes = new HashMap<>(41, 1);
        commandCodes.put(265, "AA-");
        commandCodes.put(268, "Diameter-EAP-");
        commandCodes.put(274, "Abort-Session-");
        commandCodes.put(271, "Accounting-");
        commandCodes.put(272, "Credit-Control-");
        commandCodes.put(257, "Capabilities-Exchange-");
        commandCodes.put(280, "Device-Watchdog-");
        commandCodes.put(258, "Re-Auth-");
        commandCodes.put(275, "Session-Termination-");
        commandCodes.put(283, "User-Authorization-");
        commandCodes.put(284, "Server-Assignment-");
        commandCodes.put(285, "Location-Info");
        commandCodes.put(286, "Multimedia-Auth-");
        commandCodes.put(287, "Registration-Termination-");
        commandCodes.put(288, "Push-Profile-");
        commandCodes.put(300, "User-Authorization-");
        commandCodes.put(301, "Server-Assignment-");
        commandCodes.put(302, "Location-Info-");
        commandCodes.put(303, "Multimedia-Auth-");
        commandCodes.put(304, "Registration-Termination-");
        commandCodes.put(305, "Push-Profile-");
        commandCodes.put(306, "User-Data-");
        commandCodes.put(307, "Profile-Update-");
        commandCodes.put(308, "Subscribe-Notifications-");
        commandCodes.put(309, "Push-Notification-");
        commandCodes.put(310, "Bootstrapping-Info-");
        commandCodes.put(311, "Message-Process-");
        commandCodes.put(316, "Update-Location-");
        commandCodes.put(317, "Cancel-Location-");
        commandCodes.put(318, "Authentication-Information-");
        commandCodes.put(319, "Insert-Subscriber-Data-");
        commandCodes.put(320, "Delete-Subscriber-Data-");
        commandCodes.put(321, "Purge-UE-");
        commandCodes.put(323, "Notify-");
        commandCodes.put(8388620, "Provide-Location-");
        commandCodes.put(8388622, "Routing-Info-");
        commandCodes.put(260, "AA-Mobile-Node-");
        commandCodes.put(262, "Home-Agent-MIP");
        commandCodes.put(8388718, "Configuration-Information-");
        commandCodes.put(8388719, "Reporting-Information-");
        commandCodes.put(8388726, "NIDD-Information-");

    }


}