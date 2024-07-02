package client.panels;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final JTextArea chatTextArea;
    public final JTextArea messageInput;
    public final JTextArea chatMembers;
    public final JButton button;
    public final JLabel errorLabel;
    public ChatPanel() {
        errorLabel = new JLabel();
        chatTextArea = getTextArea("Welcome to the chat\n",false);
        messageInput = getTextArea("",true);
        chatMembers = getTextArea("",false);

        button = new JButton("Send");

        JScrollPane chatScrollPane = getScrollPane(chatTextArea,200,250);
        JScrollPane inputScrollPane = getScrollPane(messageInput,137,50);
        JScrollPane membersScrollPane = getScrollPane(chatMembers,150,210);
        Box innerBox = new Box(BoxLayout.X_AXIS);
        innerBox.add(inputScrollPane);
        innerBox.add(button);

        Box chatBox = new Box(BoxLayout.Y_AXIS);
        chatBox.add(chatScrollPane);
        chatBox.add(innerBox);
        chatBox.add(errorLabel);

        JPanel panel = new JPanel();
        panel.setAlignmentY(CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Members:");
        label.setBounds(0,0,100,20);
        panel.add(label);
        panel.add(membersScrollPane);

        Box outBox = new Box(BoxLayout.X_AXIS);
        outBox.add(chatBox);
        outBox.add(Box.createHorizontalStrut(5));
        outBox.add(panel);
        add(outBox);

    }


    private JTextArea getTextArea(String text, boolean isEditable) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Dialog", Font.PLAIN, 14));
        textArea.setTabSize(4);
        textArea.setLineWrap(true);
        textArea.setEditable(isEditable);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private JScrollPane getScrollPane(JTextArea area,int w, int h) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setMaximumSize(new Dimension(w,h));
        scrollPane.setMinimumSize(new Dimension(w,h));
        scrollPane.setPreferredSize(new Dimension(w,h));
        DefaultCaret caret = (DefaultCaret)area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    public void addMessage(String message) {
        chatTextArea.append(message);
    }

}
