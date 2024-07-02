package client.panels;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {

    private final JTextField ip;
    private final JTextField username;
    private final JTextField port;
    private final JLabel error;
    public JButton confirmButton;

    public RegistrationPanel() {

        ip = getTextField(60,"localhost");
        username = getTextField(80,"");
        port = getTextField(50,"7526");
        error = new JLabel("");

        confirmButton = new JButton("Log In");
        confirmButton.setAlignmentX(JButton.LEFT_ALIGNMENT);

        Box inBoxIP = new Box(BoxLayout.X_AXIS);
        inBoxIP.add(new JLabel("IP:"));
        inBoxIP.add(ip);
        inBoxIP.add(new JLabel(":"));
        inBoxIP.add(port);

        Box inBoxUsername = new Box(BoxLayout.X_AXIS);
        inBoxUsername.add(new JLabel("Your name:"));
        inBoxUsername.add(username);

        Box outBox = new Box(BoxLayout.Y_AXIS);
        outBox.add(Box.createVerticalStrut(30));
        outBox.add(error);
        outBox.add(Box.createVerticalStrut(10));
        outBox.add(inBoxIP);
        outBox.add(Box.createVerticalStrut(10));
        outBox.add(inBoxUsername);
        outBox.add(Box.createVerticalStrut(10));

        outBox.add(confirmButton);
        add(outBox);
    }


    public void setTextError(String text) {
        error.setText(text);
    }
    public String getIp() {
        return ip.getText();
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPort() {
        return port.getText();
    }

    private JTextField getTextField(int len,String text) {
        JTextField field = new JTextField(text);
        field.setMaximumSize(new Dimension(len,20));
        field.setMinimumSize(new Dimension(len,20));
        field.setPreferredSize(new Dimension(len,20));
        return field;
    }

}
