package org.example.packets;

import io.pkts.buffer.Buffer;
import org.example.helpers.PktBuffWrapper;
import org.example.protocols.AVPFormat;

import java.io.IOException;

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

    public String getData() throws IOException{
        if(isVendorSpecific())
            System.out.println("Vendor Specific");
        return buffWrapper.getString(avpMessage, 8, ((int) avpLength() - 8));


    }



}