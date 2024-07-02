package client;


import java.net.*;
import java.io.*;

public class Client {
    Socket socket;
    MainUI ui;
    ObjectOutputStream out;
    ObjectInputStream in;
    String username;

    private Client(Socket socket,MainUI ui) {
        this.socket = socket;
        this.ui = ui;
        init();
    }

    public static Client getClient(String ip, int port, MainUI ui) {
        Socket socket =  getSocket(ip,port);
        if (socket == null)
            return null;
        return new Client(socket,ui);
    }

    private static Socket getSocket(String ip, int port) {
        Socket socket;
        System.out.println("Trying to connect to " + ip + ":" + port);
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            System.out.println("Cannot connect to " + ip + ":" + port);
            return null;
        }
        System.out.println("The connection is established.");
        return socket;
    }


    private void init() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            new MessageListener(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text) {
        try {
            out.writeObject(new Message(text,username, Message.MESSAGE_TYPE.COMMON_MSG));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsername(String newUsername) {
        try {
            out.writeObject(new Message(newUsername,null, Message.MESSAGE_TYPE.NEW_USER));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getHistory() {
        try {
            out.writeObject(new Message("", username, Message.MESSAGE_TYPE.REQUEST_HISTORY));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMembers() {
        try {
            out.writeObject(new Message("", username, Message.MESSAGE_TYPE.REQUEST_USER_LIST));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class MessageListener extends Thread {

        private final ObjectInputStream reader;

        public MessageListener(ObjectInputStream reader) {
            this.reader = reader;
            start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Message message = (Message) reader.readObject();
                    switch (message.getType()) {
                        case COMMON_MSG, CONFIRM_MSG ->
                            ui.receiveMessage(message.getData(),message.getUsername());
                        case CONFIRM_NEW_USER -> {
                            ui.usernameConfirmed();
                            username = message.getData();
                            getHistory();
                            getMembers();
                        }
                        case REJECT_NEW_USER -> ui.registrationError(message.getData());

                        case NEW_USER -> {
                            ui.addChatMember(message.getUsername());
                            ui.receiveMessage(message.getData(),"Server");
                        }
                        case REJECT_MSG -> ui.messageRejected(message.getData());
                        case CONFIRM_USER_LIST -> ui.setChatMembers(message.getData());
                        case DELETE_USER -> {
                            ui.receiveMessage(message.getData() + " left chat.","Server");
                            getMembers();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}