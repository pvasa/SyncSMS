package svyp.syncsms.models;

import java.util.List;

public class Message {

    public List<Contact> contacts;
    public String message;
    public String date;
    public boolean read;
    public int unreadCount;

    public Message(List<Contact> contacts, String message, String date) {
        this.contacts = contacts;
        this.message = message;
        this.date = date;
        this.read = false;
        this.unreadCount = 1;
    }
}
