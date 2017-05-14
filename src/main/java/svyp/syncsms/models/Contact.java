package svyp.syncsms.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    public int _id;
    public String name;
    public ArrayList<String> numbers;
    public List<Message> messages;
    public Uri photoURI;

    public Contact(int contact_id, String name, ArrayList<String> numbers, Uri photoURI) {
        this._id = contact_id;
        this.name = name;
        this.numbers = numbers;
        this.photoURI = photoURI;
    }
}
