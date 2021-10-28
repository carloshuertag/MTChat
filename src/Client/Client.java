package Client;

import Chat.MTChat;
import Chat.MTChat;
import Client.ClientGUI;
import Models.Data;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 *
 * @author huert
 */
public class Client extends Thread {
    
    private static ClientGUI gui;
    
    @Override
    public void run(){
        MulticastSocket socket = null;
        DatagramPacket packet = null;
        ByteArrayInputStream bais;
        Data data;
        ObjectInputStream ois;
        String message = "", tmp, copy, name = "";
        StringBuilder aux = new StringBuilder();
        int segment;
        byte[] buffer;
        try{
            socket = new MulticastSocket(MTChat.SERVER_PORT);
            name = gui.getUsername();
            MTChat.socketJoinGroupGUI(socket, MTChat.SERVER_PORT);
            MTChat.sendMessage(socket, packet, "<connect>" + name);
        } catch (Exception ex) {
            MTChat.fatalError(ex);
        }
        try {
            for(;;){
                if(gui.getRw()){ // write
                    aux.setLength(0);
                    if(gui.isConnected()) {
                        if(gui.getActiveTab() == 0){
                            aux.append("C<msg><");
                            aux.append(name);
                        } else {
                            aux.append("C<msg><private><");
                            aux.append(name);
                            aux.append("><");
                            aux.append(gui.getChat());
                        }
                        aux.append(">");
                        aux.append(gui.getMessage());
                    } else {
                        aux.append("<disconnect>");
                        aux.append(name);
                    }
                    gui.setRw(false);
                    MTChat.sendMessage(socket, packet, aux.toString());
                } else { //read
                    socket.setSoTimeout(100);
                    try{
                        buffer = new byte[65535];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        bais = new ByteArrayInputStream(packet.getData());
                        ois = new ObjectInputStream(bais);
                        data = (Data) ois.readObject();
                        ois.close();
                        bais.close();
                        tmp = new String(data.getData(), 0, data.getData().length);
                        segment = data.getPacketNo();
                        copy = message;
                        message = MTChat.getMessage(socket, packet, data,
                                tmp, message, segment, copy);
                        if(data.getPacketNo() == data.getTotal() - 1) {
                            gui.setNewMessage(message);
                        }
                        message = "";
                    } catch (Exception ex) { }
                }
            }
        } catch(Exception ex) {
            MTChat.fatalError(ex);
        }
    }
    
    public static void main(String args[]) {
        Client multicastChatClient = new Client();
        gui = new ClientGUI(false);
        multicastChatClient.start();
        try {
            multicastChatClient.join();
        } catch (Exception ex){
            MTChat.fatalError(ex);
        }
    }
    
}
