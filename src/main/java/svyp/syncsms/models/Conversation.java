package svyp.syncsms.models;

import java.util.List;

public class Conversation {

    public int threadId;
    public String address;
    public String snippet;
    public long lastMessageDate;
    public boolean read;
    public int unreadCount;
    public List<Message> messages;

    public Conversation(int threadId, String address, String snippet, long lastMessageDate, boolean read) {
        this.threadId = threadId;
        this.address = address;
        this.snippet = snippet;
        this.lastMessageDate = lastMessageDate;
        this.read = read;
        //this.unreadCount = unreadCount;
        //this.messages = messages;
    }
}
