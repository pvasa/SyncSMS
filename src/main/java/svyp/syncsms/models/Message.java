package svyp.syncsms.models;

public class Message {

    public int _id;
    public int threadId;
    public int type;
    public String address;
    public String body;
    public long date;
    public long dateSent;
    public boolean read;

    public Message(int _id, int threadId, int type, String address, String body,
                   long date, long dateSent, boolean read) {
        this._id = _id;
        this.threadId = threadId;
        this.type = type;
        this.address = address;
        this.body = body;
        this.date = date;
        this.dateSent = dateSent;
        this.read = read;
    }
}
