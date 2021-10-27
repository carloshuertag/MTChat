package Server;

import Chat.Properties;
import Models.Data;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
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
        NetworkInterface ni = null;
        boolean coms = true, first = true;
        String message = "", tmp, copy, aux = "", name = "";
        int segment;
        byte[] buffer;
        for(;;)
            try{
                socket = new MulticastSocket(Properties.SERVER_PORT);
                socket.setReuseAddress(true);
                if(first){
                    Properties.socketJoinGroup(socket, Properties.SERVER_PORT);
                    ni = socket.getNetworkInterface();
                    first = false;
                } else {
                    socket.joinGroup(new InetSocketAddress(
                            InetAddress.getByName(Properties.GROUP_IP),
                            Properties.SERVER_PORT), ni);
                }
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
                message = Properties.getMessage(socket, packet, data, tmp,
                        message, segment, copy);
                if(data.getPacketNo() == data.getTotal() - 1) {
                    if(message.contains("<connect>")){
                        name = "";
                        aux = message.substring(9);
                        for(int i = 0; i < aux.length() && Character.isLetter(aux.charAt(i)); i++)
                            name += aux.charAt(i);
                        if (!users.contains(name)) users.add(name);
                        aux = "<users>" + users.toString();
                        coms = true;
                    } else if(message.contains("C<msg>")){
                        aux = message.substring(1);
                        aux = "S" + aux;
                    } else if(message.contains("<disconnect>")){
                        name = "";
                        aux = message.substring(12);
                        for(int i = 0;  i < aux.length() && Character.isLetter(aux.charAt(i)); i++)
                            name += aux.charAt(i);
                        users.remove(name);
                        aux = "<users>" + users.toString();
                    } else coms = false;
                    if(coms) {
                        System.out.println("Received: " + message);
                        Properties.sendMessage(socket, packet, aux);
                        socket.close();
                        System.out.println("Sent: "+aux);
                    }
                    message = "";
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
