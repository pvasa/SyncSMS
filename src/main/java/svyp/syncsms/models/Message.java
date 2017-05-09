package svyp.syncsms.models;

import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

import java.util.ArrayList;

public class Message {

    public int _id;
    public int threadId;
    public int type;
    public String address;
    public SpannableString body;
    public long date;
    public long dateSent;
    public boolean read;
    public ArrayList<TextAppearanceSpan> spans;

    public Message(int _id, int threadId, int type, String address, SpannableString body,
                   long date, long dateSent, boolean read) {
        this._id = _id;
        this.threadId = threadId;
        this.type = type;
        this.address = address;
        this.body = body;
        this.date = date;
        this.dateSent = dateSent;
        this.read = read;
        this.spans = new ArrayList<>();
    }
}
