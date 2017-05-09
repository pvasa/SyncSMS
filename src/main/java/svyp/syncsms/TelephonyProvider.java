package svyp.syncsms;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony.Sms;
import android.text.SpannableString;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import svyp.syncsms.models.Contact;
import svyp.syncsms.models.Conversation;
import svyp.syncsms.models.Message;

public class TelephonyProvider {

    private TelephonyProvider() {}

    public static final Comparator<Message> MESSAGE_COMPARATOR = new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            return Math.max(o1.date, o1.dateSent) > Math.max(o2.date, o2.dateSent) ? 1 : -1;
        }
    };

    public static final Comparator<Conversation> CONVERSATION_COMPARATOR = new Comparator<Conversation>() {
        @Override
        public int compare(Conversation o1, Conversation o2) {
            return o1.lastMessageDate > o2.lastMessageDate ? -1 : 1;
        }
    };

    public static HashSet<Conversation> getAllConversations(ContentResolver contentResolver) {
        HashSet<Conversation> conversations = new HashSet<>();
        try (Cursor result = contentResolver.query(
                Sms.Conversations.CONTENT_URI, Constants.PROJECTION_CONVERSATION, null, null, null)) {
            if (result != null && result.moveToFirst()) {
                do {
                    int threadId = result.getInt(result.getColumnIndex(Constants.Columns.THREAD_ID.toString()));
                    Message message = getLastSMSOfConversation(contentResolver, threadId);
                    Conversation conversation = new Conversation(
                            threadId,
                            message.address,
                            result.getString(result.getColumnIndex(Constants.Columns.SNIPPET.toString())),
                            Math.max(message.date, message.dateSent),
                            message.read
                    );
                    conversations.add(conversation);
                } while (result.moveToNext());
            }
        } catch (Exception e) {e.printStackTrace();}
        return conversations;
    }

    private static Message getLastSMSOfConversation(ContentResolver contentResolver, int threadId) {
        Message message = null;
        try (Cursor result = contentResolver.query(
                Sms.CONTENT_URI,
                Constants.PROJECTION_MESSAGE,
                Sms.THREAD_ID + "=?",
                new String[]{String.valueOf(threadId)},
                Sms.DEFAULT_SORT_ORDER)) {
            if (result != null && result.moveToFirst()) {
                message = new Message(
                        result.getInt(result.getColumnIndex(Constants.Columns._ID.toString())),
                        result.getInt(result.getColumnIndex(Constants.Columns.THREAD_ID.toString())),
                        result.getInt(result.getColumnIndex(Constants.Columns.TYPE.toString())),
                        result.getString(result.getColumnIndex(Constants.Columns.ADDRESS.toString())),
                        new SpannableString(
                                result.getString(result.getColumnIndex(Constants.Columns.BODY.toString()))),
                        result.getLong(result.getColumnIndex(Constants.Columns.DATE.toString())),
                        result.getLong(result.getColumnIndex(Constants.Columns.DATE_SENT.toString())),
                        result.getInt(result.getColumnIndex(Constants.Columns.READ.toString())) == 1
                );
            }
        } catch (Exception e) {e.printStackTrace();}
        return message;
    }

    public static ArrayList<Message> getSMSMessages(ContentResolver contentResolver, int threadId) {
        ArrayList<Message> messages = new ArrayList<>();
        try (Cursor result = contentResolver.query(
                Sms.CONTENT_URI,
                Constants.PROJECTION_MESSAGE,
                Sms.THREAD_ID+"=?",
                new String[]{String.valueOf(threadId)},
                "date ASC")) {
            if (result != null && result.moveToFirst()) {
                do {
                    Message message = new Message(
                            result.getInt(result.getColumnIndex(Constants.Columns._ID.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.THREAD_ID.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.TYPE.toString())),
                            result.getString(result.getColumnIndex(Constants.Columns.ADDRESS.toString())),
                            new SpannableString(
                                    result.getString(result.getColumnIndex(Constants.Columns.BODY.toString()))),
                            result.getLong(result.getColumnIndex(Constants.Columns.DATE.toString())),
                            result.getLong(result.getColumnIndex(Constants.Columns.DATE_SENT.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.READ.toString())) == 1
                    );
                    messages.add(message);
                } while (result.moveToNext());
            }
        } catch (Exception e) {e.printStackTrace();}
        Message lastMessage = null;
        for (Message message : messages) {
            if (lastMessage != null) {
                int type = Utils.areMessagesSimilar(lastMessage, message);
                switch (type) {
                    case Constants.MT_S:
                        if (new DateTime(lastMessage.dateSent).getMinuteOfDay() ==
                                new DateTime(message.dateSent).getMinuteOfDay())
                            message.type += 10;
                        break;
                    case Constants.MT_R:
                        if (new DateTime(lastMessage.date).getMinuteOfDay() ==
                                new DateTime(message.date).getMinuteOfDay())
                            message.type += 10;
                        break;
                }
            }
            lastMessage = message;
        }
        return messages;
    }

    public static ArrayList<Contact> getAllContacts(ContentResolver cr) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try (Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {

            if (cur != null && cur.moveToFirst()) {
                contacts = new ArrayList<>();
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Uri contactURI = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                        Uri photoURI = Uri.withAppendedPath(
                                contactURI, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                        try (Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null)) {
                            ArrayList<String> numbers = new ArrayList<>();
                            while (pCur != null && pCur.moveToNext()) {
                                numbers.add(pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            }
                            contacts.add(new Contact(name, numbers, photoURI));
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public static void test(ContentResolver contentResolver) {
        try (Cursor result = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {
            if (result != null && result.moveToFirst()) {
                do {
                    Log.d("********************", "********************");
                    String[] names = result.getColumnNames();
                    for (String name : names) {
                        int index = result.getColumnIndex(name);
                        if (index > -1) {
                            Log.d("DATA", "Name: \"" + name + "\" Value: \"" + result.getString(index) + "\"");
                        }
                    }
                } while (result.moveToNext());
            }
        } catch (Exception e) {e.printStackTrace();}
    }

}
