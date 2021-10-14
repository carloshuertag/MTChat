/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;

/**
 *
 * @author huert
 */
public class Data implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static int userIDS = 1;
    private int packetNo, total, prevPacketNo, userID;
    private byte[] data;

    public Data() {
        userID = userIDS;
        userIDS++;
    }

    public Data(int packetNo, int total, int prevPacketNo, byte[] data) {
        this.packetNo = packetNo;
        this.total = total;
        this.prevPacketNo = prevPacketNo;
        this.data = data;
        userID = userIDS;
        userIDS++;
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

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data{packetNo=").append(packetNo);
        sb.append(", total=").append(total);
        sb.append(", prevPacketNo=").append(prevPacketNo);
        sb.append(", userID=").append(userID);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
    
}
