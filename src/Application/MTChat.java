package Application;

import Chat.Properties;
import Models.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author huert
 */
public class MTChat extends JFrame {

    static private class NewMessages extends Thread {

        private MulticastSocket socket;
        private DatagramPacket packet;
        private byte[] buffer;

        public NewMessages(MulticastSocket socket) {
            this.socket = socket;
        }
        
        public void setSocket(MulticastSocket socket){
            this.socket = socket;
        }

        public void run() {

        }
    }

    static private class NewUsers extends Thread {

        private final MulticastSocket socket;
        private DatagramPacket packet;
        private ByteArrayInputStream bais;
        private ObjectInputStream ois;
        private byte[] buffer;

        public NewUsers(MulticastSocket socket) {
            this.socket = socket;
        }

        public void run() {
            buffer = new byte[65535];
            packet = new DatagramPacket(buffer, buffer.length);
            int usersCount = 0;
            System.out.println("Thread initialized");
            for (;;) {
                System.out.println("Thread run");
                try {
                    System.out.println("Receiving packets from: "+socket.getInetAddress());
                    socket.receive(packet);
                    System.out.println("Datagram packet received");
                    usersCount = Integer.parseInt(new String(packet.getData(), 0,
                            packet.getLength()));
                    System.out.println("Users list size received" + usersCount);
                    System.out.println("Users count "+usersCount);
                    IntStream.range(0, usersCount).forEach(i -> {
                        try {
                            buffer = new byte[65535];
                            packet = new DatagramPacket(buffer, buffer.length);
                            socket.receive(packet);
                            bais = new ByteArrayInputStream(packet.getData());
                            ois = new ObjectInputStream(bais);
                            availableUsers.add((User) ois.readObject());
                        } catch (Exception ex) {
                            System.out.println("Error at getting NewUsers list");
                        }
                    });
                    setChatList();
                    System.out.println("Wait for 5 seconds jsjs.");
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    System.out.println("Error at getting NewUsers: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    private final JPanel mainPanel, inputPanel, startPanel;
    private static JPanel chatsPanel;
    private final JEditorPane bodyEditorPane;
    private final JPopupMenu emojisPopupMenu;
    private final List<JMenuItem> emojisMenuItems;
    private final JButton emojiButton, audioButton, sendButton, connectButton,
            disconnectButton;
    private final JTextArea messageArea;
    private final JTextField userField;
    private final JLabel statusLabel;
    private static DefaultListModel<String> chatsListModel;
    private static JList chatsList;
    private final StringBuilder htmlBuilder;
    private static JSplitPane contentSplitPane;
    private static JScrollPane scrollPane;
    private static MulticastSocket client;
    private DatagramPacket packet;
    private static List<User> availableUsers;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ByteArrayInputStream bais;
    private final ByteArrayOutputStream baos;
    private byte[] buffer;
    private User clientUser;

    public MTChat() throws IOException, InterruptedException {
        super("MTChat");
        mainPanel = new JPanel(new BorderLayout(10, 10));
        startPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chatsPanel = new JPanel();
        availableUsers = new ArrayList<>();
        chatsListModel = new DefaultListModel<>();
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        userField = new JTextField("Username");
        statusLabel = new JLabel("Disconnected");
        bodyEditorPane = new JEditorPane();
        htmlBuilder = new StringBuilder();
        inputPanel = new JPanel(new GridBagLayout());
        emojisPopupMenu = new JPopupMenu("😊");
        emojisMenuItems = new ArrayList<>();
        emojiButton = new JButton();
        messageArea = new JTextArea("Enter a message", 1, 2);
        audioButton = new JButton();
        sendButton = new JButton("Send message");
        setComponents();
        addComponents();
        setFrame();
        baos = new ByteArrayOutputStream();
    }

    public static void main(String args[]) {
        try {
            new MTChat();
            client = new MulticastSocket();
            client.setReuseAddress(true);
            client.setTimeToLive(225);
            Properties.socketJoinGroupGUI(client, Properties.CLIENTS_PORT);
            NewUsers newUsers = new NewUsers(client);
            newUsers.start();
            System.out.println("Thread started");
            newUsers.join();
        } catch (Exception ex) {
            System.out.println("Cannot run MTChat Client");
            ex.printStackTrace();
        }
    }

    private void setFrame() {
        setSize(Properties.WIDTH, Properties.HEIGHT);
        setResizable(false);
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });
    }

    private void setComponents() {
        chatsPanel.setLayout(new BoxLayout(chatsPanel, BoxLayout.PAGE_AXIS));
        bodyEditorPane.setContentType("text/html");
        setHtmlBody();
        bodyEditorPane.setText(htmlBuilder.toString());
        bodyEditorPane.setEditable(false);
        scrollPane = new JScrollPane(bodyEditorPane);
        setChatList();
        setStart();
        setInput();
    }

    private static void setChatList() {
        chatsPanel.removeAll();
        if (availableUsers.isEmpty()) {
            chatsListModel.addElement("General");
        } else {
            availableUsers.forEach(user -> {
                chatsListModel.addElement(user.getName());
            });
        }
        chatsList = new JList<>(chatsListModel);
        chatsList.addListSelectionListener(e -> setChat());
        contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                chatsList, scrollPane);
        chatsPanel.add(contentSplitPane);
    }

    private void setStart() {
        connectButton.addActionListener(e -> connect());
        disconnectButton.addActionListener(e -> disconnect());
        startPanel.add(userField);
        startPanel.add(connectButton);
        startPanel.add(statusLabel);
        startPanel.add(disconnectButton);
    }

    public void disconnect() {

    }

    public void connect() {
        try {
            clientUser = new User(userField.getText(), client.getLocalSocketAddress());
            oos = new ObjectOutputStream(baos);
            oos.writeObject(clientUser);
            oos.flush();
            buffer = baos.toByteArray();
            packet = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getByName(Properties.GROUP_IP), Properties.SERVER_PORT);
            client.send(packet);
            connectButton.setEnabled(false);
            userField.setEnabled(false);
            statusLabel.setText("Connected");
            messageArea.setText("");
            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Unable to connect, try again later",
                    "Oops " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void setChat() {

    }

    private void setHtmlBody() {
        htmlBuilder.setLength(0);
        htmlBuilder.append("<!DOCTYPE html><html>");
        htmlBuilder.append(Properties.HTMLHEAD);
        htmlBuilder.append("<body><h1>MTChat</h1><main>");
        htmlBuilder.append(Properties.HTMLMSG1START);
        htmlBuilder.append("User1: ");
        htmlBuilder.append("Message");
        htmlBuilder.append(Properties.HTMLIMGSTART);
        htmlBuilder.append(Properties.EMOJIURLS[0]);
        htmlBuilder.append(Properties.HTMLIMGEND);
        htmlBuilder.append(Properties.HTMLMSGEND);
        htmlBuilder.append(Properties.HTMLMSG2START);
        htmlBuilder.append("User2: ");
        htmlBuilder.append("Response");
        htmlBuilder.append(Properties.HTMLIMGSTART);
        htmlBuilder.append(Properties.EMOJIURLS[1]);
        htmlBuilder.append(Properties.HTMLIMGEND);
        htmlBuilder.append(Properties.HTMLMSGEND);

        htmlBuilder.append("</main></body></html>");
    }

    private void setInput() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.ipadx = 2 * Properties.WIDTH / 3;
        constraints.ipady = 25;
        inputPanel.add(messageArea, constraints);
        constraints.gridx++;
        constraints.ipadx = 0;
        setEmojis();
        inputPanel.add(emojiButton, constraints);
        constraints.gridx++;
        setAudio();
        inputPanel.add(audioButton, constraints);
        constraints.gridx++;
        inputPanel.add(sendButton, constraints);
    }

    private void setEmojis() {
        try {
            emojiButton.setIcon(new ImageIcon(ImageIO.read(new URL(
                    Properties.EMOJIURLS[0]))));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Unable to load emojis, sorry", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            emojiButton.setText("Emojis");
        }
        emojiButton.addActionListener(i -> {
            emojisPopupMenu.show(emojiButton, 0, 0);
        });
        emojiButton.setPreferredSize(new Dimension(50, 25));
        IntStream.range(0, Properties.EMOJIURLS.length).forEach(i -> {
            try {
                emojisMenuItems.add(new JMenuItem(new ImageIcon(ImageIO.read(
                        new URL(Properties.EMOJIURLS[i])))));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Unable to load emojis, sorry", "Oops" + ex.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
                IntStream.range(0, Properties.EMOJINAMES.length).forEach(j -> {
                    emojisMenuItems.add(new JMenuItem(Properties.EMOJINAMES[i]));
                });
            }
            emojisMenuItems.get(i).addActionListener(e -> appendEmoji(i));
        });
        IntStream.range(0, emojisMenuItems.size()).forEach((int i) -> {
            emojisPopupMenu.add(emojisMenuItems.get(i));
        });
    }

    private void setAudio() {
        try {
            audioButton.setIcon(new ImageIcon(ImageIO.read(new URL(
                    Properties.AUDIOICON))));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Unable to load emojis, sorry", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            audioButton.setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
        }
        audioButton.addActionListener(i -> {

        });
        audioButton.setPreferredSize(new Dimension(50, 25));
    }

    private void addComponents() {
        mainPanel.add(startPanel, BorderLayout.NORTH);
        mainPanel.add(chatsPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void appendEmoji(int index) {

    }

}
