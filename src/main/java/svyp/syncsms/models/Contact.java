package svyp.syncsms.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    public String name;
    public ArrayList<String> numbers;
    public List<Message> messages;
    public Uri photoURI;

    public Contact(String name, ArrayList<String> numbers, Uri photoURI) {
        this.name = name;
        this.numbers = numbers;
        this.photoURI = photoURI;
    }

    public Contact(String name, ArrayList<String> numbers) {
        this.name = name;
        this.numbers = numbers;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }
}
