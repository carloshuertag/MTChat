package Chat;

import Chat.Properties;
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
            socket = new MulticastSocket(Properties.SERVER_PORT);
            socket.setReuseAddress(true);
            name = gui.getUsername();
            Properties.socketJoinGroupGUI(socket, Properties.SERVER_PORT);
            Properties.sendMessage(socket, packet, "<connect>" + name);
        } catch (Exception ex) {
            Properties.fatalError(ex);
        }
        for(;;){
            try {
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
                        gui.setRw(false);
                    } else {
                        aux.append("<disconnect>");
                        aux.append(name);
                    }
                    Properties.sendMessage(socket, packet, aux.toString());
                    System.out.println("Message sent: "+aux.toString());
                } else { //read
                    socket.setSoTimeout(100);
                    socket.setTimeToLive(1);
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
                        message = Properties.getMessage(socket, packet, data,
                                tmp, message, segment, copy);
                        if(data.getPacketNo() == data.getTotal() - 1) {
                            System.out.println("Message received: "+message);
                            gui.setNewMessage(message);
                            System.out.println("New Message set");
                        }
                        message = "";
                    } catch (Exception ex) { }
                }
            } catch(Exception ex) {
                Properties.fatalError(ex);
            }
        }
    }
    
    public static void main(String args[]) {
        Client multicastChatClient = new Client();
        gui = new ClientGUI(false);
        multicastChatClient.start();
        try {
            multicastChatClient.join();
        } catch (Exception ex){
            Properties.fatalError(ex);
        }
    }
    
}
