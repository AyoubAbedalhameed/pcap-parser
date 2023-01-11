package org.example.protocols;


public enum AVPFormat implements Framer {
    AVP_CODE("AVP code", 0, 32),
    VENDOR_ID_F("Vendor ID feild is presented", 32,1),
    MANDATORY_F("Mandatory Flag", 33, 1),
    ENCRYPTION_F("Encryption Flag", 34, 1),
    AVP_LENGTH("AVP Length", 40, 24 ),
    VENDOR_ID("Vendor ID", 64, 32);






    private final String name;
    private final int index;
    private final int length;

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int getFieldLength() {
        return length;
    }

    private AVPFormat(String name, int index, int length){
        this.name = name;
        this.index = index;
        this.length = length;
    }

}