package org.example;

/* Request objects are constructed and created in the main class for each new request, contain requests parameters/options
* Setters are protected, request parameters can be changed in the main class only*/

public class Request{
    private boolean verbose;
    private String sIP;
    private String dIP;
    private int sctpSPort = -1;
    private int sctpDPort = -1;
    private long diameterCommandCode = -1;
    private long diamaterApplicationID = -1;
    private long avpCode = -1;
    private int pCount = 50;

    private int packetCounter = 0 ;
    private int avpCounter = 0;

    private String desFile;

    private boolean reqOnl;

    private boolean respOnly;

    public boolean isReqOnl() {
        return reqOnl;
    }

    protected void setReqOnl(boolean reqOnl) {
        this.reqOnl = reqOnl;
        if(reqOnl)
             this.respOnly=false;
    }

    public boolean isRespOnly() {
        return respOnly;
    }

    protected void setRespOnly(boolean respOnly) {
        this.respOnly = respOnly;
        if(respOnly)
            this.reqOnl = false;
    }

    public String getDesFile(){
        return this.desFile;
    }


    public void packetCounterInc(){
        packetCounter++;
    }

    protected void setpCount(int pCount) {
        this.pCount = pCount;
    }

    public void avpCounterInc(){
        avpCounter++;
    }


    public void pCountDecrement(){
        pCount-- ;
    }


    public int getAvpCounter(){
        return this.avpCounter;
    }

    public int getPacketCounter() {
        return packetCounter;
    }

    public int getpCount(){
        return pCount;
    }

    protected void setPacketCounter(int packetCounter) {
        this.packetCounter = packetCounter;
    }

    protected void setAvpCounter(int avpCounter) {
        this.avpCounter = avpCounter;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getSIP() {
        return sIP;
    }

    public String getDIP() {
        return dIP;
    }

    public int getSctpSPort() {
        return sctpSPort;
    }

    public int getSctpDPort() {
        return sctpDPort;
    }

    public long getDiameterCommandCode() {
        return diameterCommandCode;
    }

    public long getDiamaterApplicationID() {
        return diamaterApplicationID;
    }

    public long getAvpCode() {
        return avpCode;
    }




    protected void setDesFile(String file){
        this.desFile = file;
    }


    protected void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    protected void setSIP(String sIP) {
        this.sIP = sIP;
    }

    protected void setDIP(String dIP) {
        this.dIP = dIP;
    }

    protected void setSctpSPort(int sctpSPort) {
        this.sctpSPort = sctpSPort;
    }

    protected void setSctpDPort(int sctpDPort) {
        this.sctpDPort = sctpDPort;
    }

    protected void setDiameterCommandCode(long diameterCommandCode) {
        this.diameterCommandCode = diameterCommandCode;
    }

    protected void setDiamaterApplicationID(long diamaterApplicationID) {
        this.diamaterApplicationID = diamaterApplicationID;
    }

    protected void setAvpCode(long avpCode) {
        this.avpCode = avpCode;
    }

    public void resetAVPCounter(){
        avpCounter = 0;
    }
}