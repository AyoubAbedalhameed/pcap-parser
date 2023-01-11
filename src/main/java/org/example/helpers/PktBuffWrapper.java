package org.example.helpers;
import io.pkts.buffer.Buffer;
import io.pkts.packet.IPv4Packet;
import org.example.protocols.DiameterPFields;
import org.example.protocols.Framer;

import java.io.IOException;


public class PktBuffWrapper{

//
//    public long getLong(Buffer buffer, DiameterPFields diameterPFields) throws IOException{
//        System.out.format("%02x\n", buffer.getByte(diameterPFields.getIndex()));
//        return buffer.getUnsignedInt(diameterPFields.getIndex());
//    }

    private static PktBuffWrapper instance = new PktBuffWrapper();

    private PktBuffWrapper(){

    }

    public static PktBuffWrapper getInstance(){
        return instance;
    }

    public long getInt(Buffer buffer, Framer diameter) throws IOException {

        if( diameter.getFieldLength() < 8 || diameter.getFieldLength() > 32 )
            throw new IllegalArgumentException("Required field length =" + diameter.getFieldLength());

        if(diameter.getFieldLength() == 8)
            return buffer.getByte(diameter.getIndex() / 8);

        if(diameter.getFieldLength() == 16)
            return buffer.getUnsignedShort(diameter.getIndex() / 8);

        if (diameter.getFieldLength() == 32)
            return (int) buffer.getUnsignedInt(diameter.getIndex() / 8);

        if(diameter.getFieldLength()==24 )
            return ((buffer.getByte(diameter.getIndex() / 8) & 255) << 16) | ((buffer.getByte(diameter.getIndex() / 8 + 1) & 255) << 8) | ((buffer.getByte(diameter.getIndex() / 8 + 2) & 255));

        else throw new IllegalArgumentException("The required field length is not allowed, fieldLength = "+ diameter.getFieldLength());
    }



    public long getInt(Buffer buffer, int index, int fLength) throws IOException {

        if( fLength < 1 || fLength > 4 )
            throw new IllegalArgumentException("The required field length is not allowed, fieldLength = "+ fLength);

        if(fLength == 1)
            return buffer.getByte(index);

        if(fLength == 2)
            return buffer.getUnsignedShort(index);

        if(fLength == 3 )
            return ((buffer.getByte(index) & 255) << 16) | ((buffer.getByte(index + 1) & 255) << 8) | ((buffer.getByte(index+ 2) & 255));



        return buffer.getUnsignedInt(index);



    }


    public boolean getBool(Buffer buffer, Framer field) throws IOException{
        if (field.getIndex()%8 == 0)
            return ((buffer.getByte(field.getIndex()/8)) & -128) == -128;

        byte temp = (byte) Math.pow(2, 7- (field.getIndex()%8));
        return (buffer.getByte(field.getIndex()/8) & temp) == temp;
    }

    public String getString(Buffer buffer, int index, int length) throws IOException{
        StringBuilder data = new StringBuilder();
        for (int i= index; i< index+ length; i++){
            data.append((char) buffer.getByte(i));
        }

        return data.toString();
    }






}