package svyp.syncsms.models;

import android.net.Uri;

import java.util.List;

public class Contact {

    public String name;
    public String number;
    public List<Message> messages;
    public Uri photoURI;

    public Contact(String name, String number, Uri photoURI) {
        this.name = name;
        this.number = number;
        this.photoURI = photoURI;
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }
}
