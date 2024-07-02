package server;

import java.io.*;
import java.net.Socket;
import client.Message;

public class ClientHandler extends Thread {
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Server server;
    private final Socket socket;
    private String username;


    public ClientHandler(Socket client, Server server) {
        socket = client;
        this.server = server;
        System.out.println("New client created");
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        start();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Message msg = (Message) in.readObject();
                messageHandler(msg);
            }
        } catch (IOException | ClassNotFoundException e) {
            downService();
        }
    }

    private void messageHandler(Message message) {
        switch (message.getType()) {
            case NEW_USER -> {
                if (username == null)
                    requestUsername(message.getData());
                else
                    sendMsg(new Message(
                            "You are already registered",null,
                            Message.MESSAGE_TYPE.REJECT_NEW_USER));
            }
            case COMMON_MSG -> {
                if (username != null) {
                    server.sendEveryoneMsgExceptSender(
                            new Message(message.getData(),username,
                                    Message.MESSAGE_TYPE.COMMON_MSG),this);
                    sendMsg(new Message(
                            message.getData(),username,
                            Message.MESSAGE_TYPE.CONFIRM_MSG));
                } else
                    sendMsg(new Message(
                            "You are not registered",null,
                            Message.MESSAGE_TYPE.REJECT_MSG));
            }
            case DELETE_USER -> downService();
            case REQUEST_HISTORY -> server.getHistory(this);

            case REQUEST_USER_LIST -> server.getUsernameList(this);

        }

    }


    private void requestUsername(String newUsername) {
        boolean isValidName = server.registerNewUser(newUsername,this);
        if (!isValidName)
            sendMsg(new Message(
                    "That name is taken or forbidden",null,
                     Message.MESSAGE_TYPE.REJECT_NEW_USER));
        else {
            username = newUsername;
            server.reportAboutNewUser(this);
            sendMsg(new Message(
                    username, null,
                    Message.MESSAGE_TYPE.CONFIRM_NEW_USER));
        }
    }


    public String getUsername() {
        return username;
    }

    public void sendMsg(Message msg) {
        try {
            System.out.println("Sending " + msg);
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            downService();
        }
    }


    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
            server.removeUser(this);
        } catch (IOException ignored) {}
    }
}
