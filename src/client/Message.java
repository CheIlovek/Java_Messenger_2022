package client;

import java.io.Serializable;

public class Message implements Serializable {
    private final String data;
    private final String username;
    private final MESSAGE_TYPE type;

    public enum MESSAGE_TYPE {
        NEW_USER, CONFIRM_NEW_USER, REJECT_NEW_USER,
        DELETE_USER,
        COMMON_MSG, CONFIRM_MSG, REJECT_MSG,
        REQUEST_HISTORY,
        REQUEST_USER_LIST, CONFIRM_USER_LIST
    }
    public Message(String data, String username, MESSAGE_TYPE type) {
        this.data = data;
        this.username = username;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getData() {
        return data;
    }

    public MESSAGE_TYPE getType() {
        return type;
    }
}
