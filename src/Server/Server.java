package Server;

import Chat.Properties;
import Models.Data;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * @author huert
 */
public class Server extends Thread {
        
    @Override
    public void run() {
        ArrayList<String> users = new ArrayList<>();
        MulticastSocket socket = null;
        DatagramPacket packet;
        ByteArrayInputStream bais;
        ObjectInputStream ois;
        Data data;
        String message = "", tmp, copy, aux = "", name = "";
        int segment;
        byte[] buffer;
        try{
            socket = new MulticastSocket();
            socket.setReuseAddress(true);
            socket.setTimeToLive(225);
        } catch (Exception ex) {
            Properties.fatalError(ex);
        }
        Properties.socketJoinGroup(socket, Properties.SERVER_PORT);
        for(;;)
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
                if(segment == 0) System.out.println("Receiving...");
                message = Properties.getMessage(socket, packet, data, tmp,
                        message, segment, copy);
                if(data.getPacketNo() == data.getTotal() - 1) {
                    System.out.println(message);
                    if(message.contains("<connect>")){
                        name = "";
                        aux = message.substring(9);
                        for(int i = 0; Character.isLetter(aux.charAt(i)); i++)
                            name += aux.charAt(i);
                        users.add(name);
                        aux = "<users>" + users.toString();
                        Properties.sendMessage(socket, packet, aux);
                    } else if(message.contains("C<msg>")){
                        aux = message.substring(1);
                        aux = "S" + aux;
                        Properties.sendMessage(socket, packet, aux);
                    } else if(message.contains("<disconnect>")){
                        name = "";
                        aux = message.substring(12);
                        for(int i = 0; Character.isLetter(aux.charAt(i)); i++)
                            name += aux.charAt(i);
                        users.remove(name);
                        aux = "<contactos>" + users.toString();
                        Properties.sendMessage(socket, packet, aux);
                    }
                    System.out.println("Sent: "+aux);
                }
                Thread.sleep(5000);
            } catch (Exception ex) {
                Properties.fatalError(ex);
            }
    }
    
    public static void main(String[] args) {
        Server multicastChatServer = new Server();
        multicastChatServer.start();
        try {
            multicastChatServer.join();
        } catch (Exception ex){
            Properties.fatalError(ex);
        }
    }
}
