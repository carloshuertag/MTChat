package Client;

import Chat.MTChat;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author huert
 */
public class ClientGUI extends JFrame{
    
    private boolean rw, connected; // read/write and connected flags
    private String username;
    private final JPanel mainPanel, inputPanel;
    private final JTabbedPane chatTabs;
    private final ArrayList<String> chatUsers, users;
    private final ArrayList<JEditorPane> chatPanes;
    private final JTextArea messageArea;
    private final StringBuilder htmlBuilder;

    public ClientGUI(boolean rw) throws HeadlessException {
        super();
        this.rw = rw;
        connected = true;
        do{
            username = JOptionPane.showInputDialog(null,
                    "Please enter your chat username",
                    "MTChat username", JOptionPane.QUESTION_MESSAGE);
        } while(username.isEmpty());
        mainPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new GridBagLayout());
        chatTabs = new JTabbedPane();
        chatUsers = new ArrayList<>();
        users = new ArrayList<>();
        chatPanes = new ArrayList<>();
        messageArea = new JTextArea("Enter a message", 1, 2);
        htmlBuilder = new StringBuilder();
        setComponents();
        setFrame();
    }
    
    private void setComponents() {
        JButton newChat = new JButton("Start a private chat");
        newChat.addActionListener(e->{
            if(users.size() == 0) JOptionPane.showMessageDialog(null,
                        "No available users, try again later", "No users",
                        JOptionPane.ERROR_MESSAGE);
            else {
                List<String> usersToDisplay = users;
                usersToDisplay.remove(username);
                if(usersToDisplay.size() == 0) JOptionPane.showMessageDialog(null,
                        "No available users, try again later", "No users",
                        JOptionPane.ERROR_MESSAGE);
                else {
                    String selected = (String) JOptionPane.showInputDialog(null,
                            "New Private Chat", "Choose the user to chat with",
                            JOptionPane.QUESTION_MESSAGE,
                            UIManager.getIcon("OptionPane.questionIcon"),
                            usersToDisplay.toArray(), usersToDisplay.toArray()[0]);
                    if(selected != null){
                        if(chatUsers.contains(selected))
                            chatTabs.setSelectedIndex(chatUsers.indexOf(selected));
                        else{
                            newChat(selected);
                            chatTabs.setSelectedIndex(chatUsers.indexOf(selected));
                        }
                        rw = false;
                    }
                }
            }
        });
        mainPanel.add(newChat, BorderLayout.NORTH);
        newChat("General");
        mainPanel.add(chatTabs, BorderLayout.CENTER);
        setInputs();
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
    
    public void newChat(String name){
        JPanel chatPanel = new JPanel();
        chatTabs.addTab(name, chatPanel);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        htmlBuilder.setLength(0);
        htmlBuilder.append("<!DOCTYPE html><html>");
        htmlBuilder.append(MTChat.HTMLHEAD);
        htmlBuilder.append("<body><h1>Chatting with: ");
        htmlBuilder.append(name);
        htmlBuilder.append("</h1><main></main></body></html>");
        editorPane.setText(htmlBuilder.toString());
        editorPane.setEditable(false);
        chatPanes.add(editorPane);
        JScrollPane scrollPane = new JScrollPane(chatPanes.get(
                chatPanes.size() - 1));
        chatPanel.add(scrollPane);
        chatUsers.add(name);
        messageArea.setText("");
    }
    
    private void setInputs() {
        JButton sendButton = new JButton("Send message");
        sendButton.addActionListener(e->{
            rw = true;
        });
        JButton emojiButton = new JButton("😊");
        JButton audioButton = new JButton();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.ipadx = 2 * MTChat.WIDTH / 3;
        constraints.ipady = 25;
        messageArea.setEditable(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        inputPanel.add(messageScroll, constraints);
        constraints.gridx++;
        constraints.ipadx = 0;
        setEmojis(emojiButton);
        inputPanel.add(emojiButton, constraints);
        constraints.gridx++;
        setAudio(audioButton);
        inputPanel.add(audioButton, constraints);
        constraints.gridx++;
        inputPanel.add(sendButton, constraints);
    }
    
    private void setEmojis(JButton emojiButton) {
        JPopupMenu emojisPopupMenu = new JPopupMenu("😊");;
        List<JMenuItem> emojisMenuItems = new ArrayList<>();
        try {
            emojiButton.setIcon(new ImageIcon(ImageIO.read(new URL(
                    MTChat.EMOJIURLS[0]))));
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
        IntStream.range(0, MTChat.EMOJIURLS.length).forEach(i -> {
            try {
                emojisMenuItems.add(new JMenuItem(new ImageIcon(ImageIO.read(new URL(MTChat.EMOJIURLS[i])))));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Unable to load emojis, sorry", "Oops" + ex.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
                IntStream.range(0, MTChat.EMOJINAMES.length).forEach(j -> {
                    emojisMenuItems.add(new JMenuItem(MTChat.EMOJINAMES[i]));
                });
            }
            emojisMenuItems.get(i).addActionListener(e -> appendEmoji(i));
        });
        IntStream.range(0, emojisMenuItems.size()).forEach((int i) -> {
            emojisPopupMenu.add(emojisMenuItems.get(i));
        });
    }
    
    private void appendEmoji(int selectedIndex) {
        htmlBuilder.setLength(0);
        htmlBuilder.append(MTChat.HTMLIMGSTART);
        htmlBuilder.append(MTChat.EMOJIURLS[selectedIndex]);
        htmlBuilder.append(MTChat.HTMLIMGEND);
        messageArea.setText(htmlBuilder.toString());
        rw = true;
    }
    
    private void setAudio(JButton audioButton) {
        try {
            audioButton.setIcon(new ImageIcon(ImageIO.read(new URL(
                    MTChat.AUDIOICON))));
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
    
    private void setFrame() {
        setTitle("MTChat: " + username);
        setSize(MTChat.WIDTH, MTChat.HEIGHT);
        setResizable(false);
        setVisible(true);
        setAutoRequestFocus(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                rw = true;
                connected = false;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    MTChat.fatalError(ex);
                }
                dispose();
                System.exit(0);
            }
        });
    }
    
    public void setNewMessage(String message){
        String user = "";
        if (message.contains("<users>")){
            users.clear();
            for(int i = 8; i < message.length(); i++)
                if(Character.isLetter(message.charAt(i)))
                    user += message.charAt(i);
                else if(message.charAt(i) == ',' || message.charAt(i) == ']'){
                    users.add(user);
                    user = "";
                }
        } else if (message.startsWith("S<msg>")) {
            String dst = "";
            message = message.substring(6);
            int i = message.contains("<private>") ? message.indexOf('<', 1): 0;
            int max = message.indexOf('>', ++i);
            for(i = i; i < max; i++) user += message.charAt(i);
            if(message.contains("<private>")) {
                message = message.substring(++i);
                for(i = 1;  i < message.length() && Character.isLetter(message.charAt(i)); i++)
                    dst += message.charAt(i);
                message = message.substring(++i);
                if(username.equals(dst))
                    if(chatUsers.contains(user))
                        displayNewMessage(chatUsers.indexOf(user), false,
                                user, message);
                    else {
                        newChat(user);
                        displayNewMessage(chatTabs.getTabCount() - 1, false,
                                user, message);
                    }
                else if (username.equals(user))
                    displayNewMessage(chatTabs.getSelectedIndex(), true, user,
                            message);
            } else
                displayNewMessage(0, user.equals(username), user, message);
        }
    }
    
    public void displayNewMessage(int index, boolean response, String user,
            String message) {
        htmlBuilder.setLength(0);
        String tmp = chatPanes.get(index).getText();
        tmp = tmp.replace("</main>", "").replace("</body>", "").replace("</html>", "");
        htmlBuilder.append(tmp);
        if(response) htmlBuilder.append(MTChat.HTMLMSG2START);
        else htmlBuilder.append(MTChat.HTMLMSG1START);
        htmlBuilder.append(user);
        htmlBuilder.append(" says: ");
        htmlBuilder.append(message);
        htmlBuilder.append(MTChat.HTMLMSGEND);
        htmlBuilder.append("</main></body></html>");
        chatPanes.get(index).setText(htmlBuilder.toString());
        repaint();
    }

    public boolean getRw() {
        return rw;
    }

    public void setRw(boolean rw) {
        this.rw = rw;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getMessage(){
        String msg = messageArea.getText();
        messageArea.setText("");
        return msg;
    }
    
    public int getActiveTab(){
         return chatTabs.getSelectedIndex();
    }
    
    public String getChat(){
        return chatUsers.get(chatTabs.getSelectedIndex());
    }
    
}
