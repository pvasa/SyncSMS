package svyp.syncsms.models;

public class Message {

    public String name;
    public String number;
    public String message;
    public String date;
    public boolean read;
    public int unreadCount;

    public Message(String name, String number, String message, String date) {
        this.name = name;
        this.number = number;
        this.message = message;
        this.date = date;
        this.read = false;
        this.unreadCount = 1;
    }
}
