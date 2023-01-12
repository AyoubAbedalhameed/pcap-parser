package org.example.handlers;

import io.pkts.PacketHandler;
import io.pkts.buffer.Buffer;
import io.pkts.packet.IPPacket;
import io.pkts.packet.IPv4Packet;
import io.pkts.packet.Packet;
import io.pkts.packet.PacketParseException;
import io.pkts.packet.sctp.SctpChunk;
import io.pkts.packet.sctp.SctpPacket;
import io.pkts.protocol.Protocol;
import org.example.Request;
import org.example.helpers.Printer;
import org.example.packets.AvpPacket;
import org.example.packets.DiameterPacket;
import java.io.IOException;
import java.util.List;

/*This class implements the io.pkts PacketHandler interface, it defines one method only (boolean nextPacket(..))
* that is a dependency of the io.pkts pcap class, hence it has a Request dependency, new Handler should
* be constructed for each new request */

public class GlobalHandler implements PacketHandler {

    private final Request request;

    public GlobalHandler(Request request){
        this.request = request;
    }

    private boolean ipFilter(IPPacket iPv4Packet){
        if(  request.getSIP() != null && (!iPv4Packet.getSourceIP().equals(request.getSIP())) ) return false;
        return (request.getDIP()==null) || (iPv4Packet.getDestinationIP().equals(request.getDIP()));
    }

    private boolean scpFilter(SctpPacket sctpPacket){
        if(  (request.getSctpSPort() != -1) && sctpPacket.getSourcePort() != request.getSctpSPort()) return false;
        return (request.getSctpDPort()==-1) || (sctpPacket.getDestinationPort() == request.getSctpDPort());
    }


    private boolean diameterFilter(DiameterPacket diameterPacket) throws IOException{
        if(request.isReqOnl() && (!diameterPacket.isRequest())) return false;
        if(request.isRespOnly() && (diameterPacket.isRequest())) return false;


        if( (request.getDiameterCommandCode() != -1) && request.getDiameterCommandCode() != diameterPacket.commandCode()) return false;

        return ( request.getDiamaterApplicationID() == -1 || request.getDiamaterApplicationID() == diameterPacket.applicationID());
    }

    private boolean avpFilter(AvpPacket avpPacket) throws IOException{
        return (request.getAvpCode() == -1 || request.getAvpCode() == avpPacket.avpCode());
    }


    public boolean nextPacket(Packet packet) throws IOException{
        if(request.getpCount() < 1) return false;

        if (packet.hasProtocol(Protocol.SCTP)) {
            IPv4Packet iPv4Packet = (IPv4Packet) packet.getPacket(Protocol.IPv4);
            if(!ipFilter(iPv4Packet)) return true;

            SctpPacket sctpPacket = null;

            try {
                sctpPacket = (SctpPacket) iPv4Packet.getPacket(Protocol.SCTP);

            }catch (PacketParseException e){
                System.err.println("Error while parsing SCTP");
                System.exit(-1);
            }

            if(!scpFilter(sctpPacket)) return true;



            List<SctpChunk> chunksList =  sctpPacket.getChunks();
            Buffer chunkData = chunksList.get(0).getValue();

            //TODO: Print the following fields in the verbose mode.
            //Verbose mode is not implemented yet
            Buffer chunkTSN = chunkData.readBytes(4);
            Buffer chunkSID = chunkData.readBytes(2);
            Buffer chunkSN = chunkData.readBytes(2);
            Buffer chunkPPID = chunkData.readBytes(4);


            Buffer diameter = chunkData.readBytes(chunkData.getReadableBytes());
            DiameterPacket diameterMessage = new DiameterPacket(diameter);
            if(!diameterFilter(diameterMessage)) return true;

            request.packetCounterInc();
            request.pCountDecrement();

            Printer.print(request, iPv4Packet, diameterMessage);


            AvpPacket avpPacket = null;
            while (true) {
                try{
                    avpPacket = diameterMessage.nextAVP();

                } catch (Exception e){
                    break;
                }

                if (!avpFilter(avpPacket)) continue;
                request.avpCounterInc();
                Printer.print(request, avpPacket);
            }

            request.resetAVPCounter();

        }

        return true;
    }

}