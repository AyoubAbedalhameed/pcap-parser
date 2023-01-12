package org.example.packets;
import io.pkts.buffer.Buffer;
import org.example.helpers.PktBuffWrapper;
import org.example.protocols.AVPFormat;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/*AvpPacket objects simply wraps one full avp buffer, maybe the name is misleading (#Packet)
 * but because it follows the same concept and usage of the DiameterPacket I preferred to use the same convention,
 * multiple getters are offered or getting avp-specific fields,
 * most of the gettors delegate a call for the BufferWrapper dependency, which is nothing
 * but an abstraction of the io.pkts buffer, it just provides me my case-specific usages.*/

public class AvpPacket {
    public final Buffer avpMessage;
    private final PktBuffWrapper buffWrapper= PktBuffWrapper.getInstance();

    public AvpPacket(Buffer avpBuffer){
        this.avpMessage = avpBuffer;
    }


    public long avpCode() throws IOException {
        return buffWrapper.getInt(avpMessage, AVPFormat.AVP_CODE);
    }

    public long avpLength() throws IOException{
        return buffWrapper.getInt(avpMessage, AVPFormat.AVP_LENGTH);
    }

    public boolean isVendorSpecific() throws IOException{
        return buffWrapper.getBool(avpMessage, AVPFormat.VENDOR_ID_F);
    }

    public boolean isMandatory() throws IOException{
        return buffWrapper.getBool(avpMessage, AVPFormat.MANDATORY_F);
    }


    public boolean encryptionFlag() throws IOException{
        return buffWrapper.getBool(avpMessage, AVPFormat.ENCRYPTION_F);
    }

    public long vendorID() throws IOException{
        if(isVendorSpecific())
            return buffWrapper.getInt(avpMessage, AVPFormat.VENDOR_ID);
        else return 0;
    }

    public String getData(boolean string) throws IOException{
        if(string){
            if(!isVendorSpecific())
                 return buffWrapper.getString(avpMessage, 8, ((int) avpLength() - 8));

            return buffWrapper.getString(avpMessage, 8 + 4, ((int) avpLength() - 12));
        }

        if(!isVendorSpecific())
            return buffWrapper.getVal(avpMessage, 8, ((int) avpLength() - 8));

        return buffWrapper.getVal(avpMessage, 8 + 4, ((int) avpLength() - 12));
    }

    public String getAttributeName() throws IOException{
        return attributeNames.get( Integer.valueOf( (int) avpCode()) );
    }




    /*TODO Here I should just provide a text file contains the mapping of al attributes, and just read it when it is required
    *  But that requires installation script/setup before using the JAR*/

    public static Map<Object, String> attributeNames = null;
    static {
        attributeNames = new HashMap<>(40, 1);
        attributeNames.put(257, "Host-IP-Address");
        attributeNames.put(258, "Auth-Application-Id");
        attributeNames.put(259, "Acct-Application-Id");
        attributeNames.put(260, "Vendor-Specific-Application-Id");
        attributeNames.put(261, "Redirect-Host-Usage");
        attributeNames.put(262, "Redirect-Max-Cache-Time");
        attributeNames.put(263, "Session-Id#");
        attributeNames.put(264, "Origin-Host#");
        attributeNames.put(265, "Supported-Vendor-Id");
        attributeNames.put(266, "Vendor-Id");
        attributeNames.put(267, "Firmware-Version");
        attributeNames.put(268, "Result-Code");
        attributeNames.put(269, "Product-Name");
        attributeNames.put(270, "Session-Binding");
        attributeNames.put(271, "Session-Server-Failover");
        attributeNames.put(272, "Multi-Round-Time-Out");
        attributeNames.put(273, "Disconnect-Cause");
        attributeNames.put(274, "Auth-Request-Type");
        attributeNames.put(296, "Origin-Realm#");
        attributeNames.put(283, "Destination-Realm#");
        attributeNames.put(293, "Destination-Host#");
        attributeNames.put(282, "Route-Record#");
    }




}