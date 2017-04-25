package svyp.syncsms.models;

import java.util.List;

public class Contact {

    public String name;
    public String number;
    public List<Message> messages;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
