package Chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author huert
 */
public class Properties {

    public static final String GROUP_IP = "228.1.1.1";
    public static final int SERVER_PORT = 8888;
    public static final int CLIENTS_PORT = 8889;
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    public static final String HTMLHEAD = "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" charset=\"UTF-8\">"
            + "<style>body {margin: 0 auto; max-width: 800px; padding: 0 20px;font-family: Arial, Helvetica, sans-serif;font-size:large}"
            + ".container {border-width: 2px; border-style: solid; border-color: #ddd; background-color: #eee; border-radius: 5px; padding: 10px; margin: 10px 0;}"
            + ".darker {border-color: #ccc; background-color: #ddd;}"
            + ".container::after {content: \"\"; clear: both; display: table;}"
            + ".container img {float: left; max-width: 60px; width: 100%; margin-right: 20px; border-radius: 50%;}"
            + ".container img.right {float: right; margin-left: 20px; margin-right:0;}</style></head>";
    public static final String HTMLMSG1START = "<div class=\"container darker\"><p><img class=\"right\" width=24 height=24 src=\""
            + "https://vk.com/images/emoji/2709_2x.png\"/>&nbsp;&nbsp;";
    public static final String HTMLMSG2START = "<div class=\"container\"><p><img width=24 height=24 src=\""
            + "https://vk.com/images/emoji/2709_2x.png\"/>&nbsp;&nbsp;";
    public static final String HTMLMSGEND = "</p></div>";
    public static final String HTMLIMGSTART = "<img width=25 height=25 alt=\"emoji\" src=\"";
    public static final String HTMLIMGEND = "\" />";
    public static final String[] EMOJIURLS = {
        "https://vk.com/images/emoji/D83DDE00_2x.png",
        "https://vk.com/images/emoji/D83DDE02_2x.png",
        "https://vk.com/images/emoji/D83DDE04_2x.png",
        "https://vk.com/images/emoji/D83DDE09_2x.png",
        "https://vk.com/images/emoji/D83DDE0A_2x.png",
        "https://vk.com/images/emoji/D83DDE0D_2x.png",
        "https://vk.com/images/emoji/D83DDE18_2x.png",
        "https://vk.com/images/emoji/D83DDE31_2x.png",
        "https://vk.com/images/emoji/D83DDE2A_2x.png"
    }, EMOJINAMES = {
        "Happy",
        "Lmao",
        "Lol",
        "Wink",
        "Smile",
        "Love",
        "Kiss",
        "Surprised",
        "Sad"
    };
    public static final String AUDIOICON = "https://vk.com/images/emoji/D83DDCE3_2x.png";
    
    public static void socketJoinGroup(MulticastSocket ms, int port){
        try{
            Collections.list(NetworkInterface.getNetworkInterfaces()).forEach(
                networkInterface -> {
                    Properties.displayNetInterfaceInfo(networkInterface);
            });
            System.out.print("\nElige la interfaz multicast (0-): ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int interfaz = Integer.parseInt(br.readLine());
            br.close();
            NetworkInterface ni = NetworkInterface.getByIndex(interfaz);
            System.out.println("\nElegiste "+ni.getDisplayName());
            ms.joinGroup(new InetSocketAddress(InetAddress.getByName(
                    Properties.GROUP_IP), port), ni);
        } catch (Exception ex){
            System.out.println("Falal error: " + ex.getMessage());
        }
    }
    
    public static void socketJoinGroupGUI(MulticastSocket ms, int port){
        try{
            List<NetworkInterface> networkInterfaces = new ArrayList<>();
            List<String> displayNames = new ArrayList<>();
            Collections.list(NetworkInterface.getNetworkInterfaces()).forEach(
                networkInterface -> {
                    try{
                        if(networkInterface.supportsMulticast()){
                            displayNames.add(networkInterface.getDisplayName());
                            networkInterfaces.add(networkInterface);
                        }
                    } catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "Couldn't join group",
                    "Oops " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                    }
            });
            String displayName = (String)JOptionPane.showInputDialog(null,
                    "Elige la interfaz multicast:", "Conectar con el servidor",
                    JOptionPane.QUESTION_MESSAGE,
                    UIManager.getIcon("OptionPane.questionIcon"),
                    displayNames.toArray(), displayNames.get(4));
            ms.joinGroup(new InetSocketAddress(InetAddress.getByName(
                    Properties.GROUP_IP), port), networkInterfaces.get(
                            displayNames.indexOf(displayName)));
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Couldn't join group",
                    "Oops " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void displayNetInterfaceInfo(NetworkInterface networkInterface) {
        System.out.printf("Nombre de despliegue: %s\n", networkInterface.getDisplayName());
        System.out.printf("Nombre: %s\n", networkInterface.getName());
        String multicast;
        try {
            multicast = (networkInterface.supportsMulticast())?"Soporta multicast":"No soporta multicast";
            System.out.printf("Multicast: %s\n", multicast);
        } catch (SocketException ex) {
            System.out.println("Falal error: " + ex.getMessage());
            
        }
        Collections.list(networkInterface.getInetAddresses()).forEach(inetAddress->{
            System.out.printf("Direccion: %s\n", inetAddress);
        });
    }
    
}
