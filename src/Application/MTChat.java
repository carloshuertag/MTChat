package Application;

import Chat.Properties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
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

    private final JPanel mainPanel, inputPanel, startPanel, chatsPanel;
    private final JEditorPane bodyEditorPane;
    private final JPopupMenu emojisPopupMenu;
    private final List<JMenuItem> emojisMenuItems;
    private final JButton emojiButton, audioButton, sendButton, connectButton,
            disconnectButton;
    private final JTextArea messageArea;
    private final JTextField userField;
    private final JLabel statusLabel;
    private final DefaultListModel<String> chatsListModel;
    private JList chatsList;
    private final StringBuilder htmlBuilder;
    private JSplitPane contentSplitPane;
    private JScrollPane scrollPane;

    public MTChat() {
        super("MTChat");
        mainPanel = new JPanel(new BorderLayout(10, 10));
        startPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chatsPanel = new JPanel();
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
        audioButton = new JButton(UIManager.getIcon("FileView.hardDriveIcon"));
        sendButton = new JButton("Send message");
        setComponents();
        addComponents();
        setFrame();
    }

    public static void main(String args[]) {
        new MTChat();
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
        chatsPanel.setLayout(new BoxLayout (chatsPanel, BoxLayout.PAGE_AXIS));
        chatsListModel.addElement("General");
        chatsList = new JList<>(chatsListModel);
        chatsList.addListSelectionListener(e -> setChat());
        chatsList.setSize(chatsPanel.getSize());
        bodyEditorPane.setContentType("text/html");
        setHtmlBody();
        bodyEditorPane.setText(htmlBuilder.toString());
        bodyEditorPane.setEditable(false);
        scrollPane = new JScrollPane(bodyEditorPane);
        contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                chatsList, scrollPane);
        chatsPanel.add(contentSplitPane);
        setStart();
        setInput();
    }
    
    private void setStart() {
        connectButton.addActionListener(e -> connect());
        disconnectButton.addActionListener(e -> disconnect());
        startPanel.add(userField);
        startPanel.add(connectButton);
        startPanel.add(statusLabel);
        startPanel.add(disconnectButton);
    }
    
    public void disconnect(){
        
    }
    
    public void connect() {
        
    }
    
    public void setChat(){
        
    }
    
    public void setHtmlBody(){
        htmlBuilder.setLength(0);
        htmlBuilder.append("<!DOCTYPE html><html>");
        htmlBuilder.append(Properties.HTMLHEAD);
        htmlBuilder.append("<body><h1>MTChat</h1>");
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
        htmlBuilder.append("</body></html>");
    }
    
    private void setInput(){
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

    private void addComponents() {
        mainPanel.add(startPanel, BorderLayout.NORTH);
        mainPanel.add(chatsPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void appendEmoji(int index) {

    }

}
