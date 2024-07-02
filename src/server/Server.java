package server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import client.Message;

public class Server {
    final int SERVER_PORT = 7526;
    final int MAX_HISTORY_LEN = 10;
    LinkedList<ClientHandler> chattingClients;
    LinkedList<ClientHandler> registeringClients;
    LinkedList<Message> messagesHistory;




    public Server() {
        messagesHistory = new LinkedList<>();
        chattingClients = new LinkedList<>();
        registeringClients = new LinkedList<>();
        waitingNewClients();
    }

    private void waitingNewClients() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (!Thread.interrupted()) {
                System.out.println("Waiting for new clients");
                Socket newClient = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(newClient,this);
                registeringClients.push(clientHandler);
                System.out.println("New client is registering");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerNewUser(String name, ClientHandler regClient) {
        if (name == null || name.trim().equals("") || name.length() > 10)
            return false;
        for (ClientHandler client : chattingClients) {
            if (name.equals(client.getUsername()))
                return false;
        }
        registeringClients.remove(regClient);
        chattingClients.push(regClient);
        System.out.println("New user: " + name);
        return true;
    }

    public void reportAboutNewUser(ClientHandler client) {
        String name = client.getUsername();
        Message msg = new Message(name + " joined chat", name, Message.MESSAGE_TYPE.NEW_USER);
        sendEveryoneMsgExceptSender(msg,client);
    }

    public void removeUser(ClientHandler client) {
        chattingClients.remove(client);
        Message formattedMsg = formatMsg(client.getUsername(),null, Message.MESSAGE_TYPE.DELETE_USER);
        sendEveryoneMsg(formattedMsg);
    }

    public void sendEveryoneMsg(Message msg) {
        sendEveryoneMsgExceptSender(msg,null);
    }

    public void sendEveryoneMsgExceptSender(Message msg,ClientHandler sender) {
        messagesHistory.addLast(msg);
        if (messagesHistory.size() >= MAX_HISTORY_LEN)
            messagesHistory.pollFirst();
        for (ClientHandler client : chattingClients) {
            if (client == sender)
                continue;
            System.out.println("Sending " + msg + " to " + client.getUsername());
            client.sendMsg(msg);
        }
    }

    public Message formatMsg(String msg, String username, Message.MESSAGE_TYPE type) {
        return new Message(msg,username, type);
    }


    public static void main(String[] args) {
        new Server();
    }


    public void getHistory(ClientHandler clientHandler) {
        if (!messagesHistory.isEmpty())
            for (Message msg : messagesHistory) {
                clientHandler.sendMsg(msg);
            }
    }


    public void getUsernameList(ClientHandler clientHandler) {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler client : chattingClients) {
            sb.append(client.getUsername()).append('\n');
        }
        Message msg = new Message(sb.toString(), null,
                 Message.MESSAGE_TYPE.CONFIRM_USER_LIST);
        clientHandler.sendMsg(msg);
    }
}