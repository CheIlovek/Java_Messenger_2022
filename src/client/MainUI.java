package client;

import client.panels.ChatPanel;
import client.panels.RegistrationPanel;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    private final int MAIN_WINDOW_HEIGHT = 350;
    private final int MAIN_WINDOW_WIDTH = 380;

    private final int REG_WINDOW_HEIGHT = 200;
    private final int REG_WINDOW_WIDTH = 250;


    private ChatPanel chatPanel;
    private RegistrationPanel registrationPanel;
    private JFrame mainFrame;
    private JFrame registrationFrame;
    private Client client;

    public MainUI() {
        init();
    }



    private void init() {

        mainFrame = getFrame(MAIN_WINDOW_WIDTH,MAIN_WINDOW_HEIGHT, "Chat");
        mainFrame.setVisible(false);
        chatPanel = getChatPanel();
        mainFrame.add(chatPanel);
        mainFrame.revalidate();

        registrationFrame = getFrame(REG_WINDOW_WIDTH,REG_WINDOW_HEIGHT, "Registration");
        registrationPanel = getRegPanel();
        registrationFrame.add(registrationPanel);

    }

    public static void main(String[] args) { new MainUI(); }
    public void receiveMessage(String message,String username) {
        String resString = username + ": " + message + '\n';
        chatPanel.addMessage(resString);
    }

    public void usernameConfirmed() {
        if (registrationFrame.isVisible()) {
            hideRegistration();
        }
    }

    public void setChatMembers(String members) {
        chatPanel.chatMembers.setText(members);
    }

    public void addChatMember(String newMember) {
        chatPanel.chatMembers.append(newMember+'\n');
    }

    public void messageRejected(String reason) {
        chatPanel.errorLabel.setText(reason);
    }

    public void hideRegistration() {
        registrationFrame.setVisible(false);
        mainFrame.setVisible(true);
    }

    private ChatPanel getChatPanel() {
        ChatPanel panel = new ChatPanel();
        panel.setBounds(0,0,MAIN_WINDOW_WIDTH,MAIN_WINDOW_HEIGHT);
        panel.button.addActionListener(e -> {
            String message = panel.messageInput.getText();
            panel.messageInput.setText("");
            client.sendMessage(message);
        });
        return panel;

    }


    private RegistrationPanel getRegPanel() {
        RegistrationPanel panel = new RegistrationPanel();
        panel.setBounds(0,0,MAIN_WINDOW_WIDTH,MAIN_WINDOW_HEIGHT);
        panel.confirmButton.addActionListener(e -> {
            client = Client.getClient(panel.getIp(),Integer.parseInt(panel.getPort()),this);
            if (client != null)
                client.setUsername(panel.getUsername());
        });
        return panel;

    }

    private JFrame getFrame(int w, int h, String title) {
        JFrame frame = new JFrame();
        frame.setResizable(false);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        frame.setBounds(
                dim.width/2 - w/2, dim.height/2 - h/2,
                w, h);
        frame.setTitle(title);
        return frame;
    }

    public void registrationError(String firstData) {
        registrationPanel.setTextError(firstData);
    }
}
