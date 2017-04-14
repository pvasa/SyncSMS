package svyp.syncsms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import svyp.syncsms.models.Contact;
import svyp.syncsms.models.Message;

public class Constants {
    public static final int RC_PERMISSIONS_NEW_MESSAGE_ACTIVITY = 0;

    public static final String KEY_TITLE = "title";

    public static final List<Contact> CONTACTS = new ArrayList<Contact>() {{
        add(new Contact("Ryan", "6476666666"));
        add(new Contact("Priyank", "5195555555"));
    }};

    public static final List<Message> MESSAGES = new ArrayList<Message>() {{
        add(new Message(Constants.CONTACTS,
                "This is a test message to check how the app looks.",
                new Date().toString()));
        add(new Message(
                new ArrayList<Contact>() {{
                    add(Constants.CONTACTS.get(0));
                }},
                "This is another test message to check how a very lengthy message looks on the app looks.",
                new Date().toString()));
    }};
}
