package Models;

import java.io.Serializable;

/**
 *
 * @author huert
 */
public class Data implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int packetNo, total, prevPacketNo;
    private byte[] data;

    public Data() {
    }

    public Data(int packetNo, int total, int prevPacketNo, byte[] data) {
        this.packetNo = packetNo;
        this.total = total;
        this.prevPacketNo = prevPacketNo;
        this.data = data;
    }

    public int getPacketNo() {
        return packetNo;
    }

    public void setPacketNo(int packetNo) {
        this.packetNo = packetNo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPrevPacketNo() {
        return prevPacketNo;
    }

    public void setPrevPacketNo(int prevPacketNo) {
        this.prevPacketNo = prevPacketNo;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data{packetNo=").append(packetNo);
        sb.append(", total=").append(total);
        sb.append(", prevPacketNo=").append(prevPacketNo);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
    
}
