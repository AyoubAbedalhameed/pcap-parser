package org.example.protocols;
public enum DiameterPFields implements Framer{
    VERSION("version", 0, 8, false, Dt.UNSIGNED_INT),
    MESSAGE_LENGTH("message-length", 8, 24, false, Dt.UNSIGNED_INT),
    MESSAGE_TYPE("message-type", 32, 1, false, Dt.BOOL),
    PROXIABLE("Proxiable", 33, 1, false, Dt.BOOL),
    ERROR("Error", 34, 1, false, Dt.BOOL),
    RETRANSMITTED("Potentially re-transmitted message", 35, 1,  false, Dt.BOOL),
    COMMAND_CODE("Command code", 40, 24, false, Dt.UNSIGNED_INT),
    APPLICATION_ID("application ID", 64, 32, false, Dt.UNSIGNED_INT),
    HHID("hop-by-hop ID", 96, 32, false, Dt.UNSIGNED_INT),
    EEID("end-to-end ID", 128, 32, false, Dt.UNSIGNED_INT);


    public int getIndex() {
        return index;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public boolean isRedundant() {
        return redundant;
    }

    public String getName() {
        return name;
    }

    public Dt getDt() {
        return dt;
    }

    private final int index;
    private final int fieldLength;
    private final boolean redundant;
    private final String name;
    private final Dt dt;







    enum Dt {
        BOOL,
        STRING,
        UNSIGNED_INT

    }








    private DiameterPFields(String name, int index, int blength, boolean redundant, Dt dt){
        this.name = name;
        this.index = index;
        this.fieldLength = blength;
        this.redundant = redundant;
        this.dt=dt;
    }



}