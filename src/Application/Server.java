package Application;

import Chat.Properties;
import Models.User;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huert
 */
public class Server {
    
    private static MulticastSocket server;
    private static DatagramPacket packet;
    private static List<User> users;
    
    public static void main(String[] args) {
        users = new ArrayList<>();
        byte[] buffer;
        try{
            server = new MulticastSocket(Properties.SERVER_PORT);
            server.setReuseAddress(true);
            server.setTimeToLive(225);
            Properties.socketJoinGroup(server, InetAddress.getByName(
                    Properties.GROUP_IP), false);
            for(;;){
                buffer = new byte[35535];
                packet = new DatagramPacket(buffer, buffer.length);
                server.receive(packet);
                
            }
        } catch(Exception ex){
            System.out.println("Cannot run server: " + ex.getMessage());
        }
    }
    
}
