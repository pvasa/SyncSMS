package svyp.syncsms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public static List<Conversation> getAllConversations(ContentResolver contentResolver) {
        List<Conversation> conversations = new ArrayList<>();
        try (Cursor result = contentResolver.query(
                Telephony.MmsSms.CONTENT_CONVERSATIONS_URI, Constants.PROJECTION_CONVERSATION, null, null, null)) {
            if (result != null && result.moveToFirst()) {
                do {
                    Conversation conversation = new Conversation(
                            result.getInt(result.getColumnIndex(Constants.Columns.THREAD_ID.toString())),
                            result.getString(result.getColumnIndex(Constants.Columns.ADDRESS.toString())),
                            result.getString(result.getColumnIndex(Constants.Columns.BODY.toString())),
                            Math.max(result.getLong(result.getColumnIndex(Constants.Columns.DATE.toString())),
                                    result.getLong(result.getColumnIndex(Constants.Columns.DATE_SENT.toString()))),
                            result.getInt(result.getColumnIndex(Constants.Columns.READ.toString())) == 1
                    );
                    conversations.add(conversation);
                } while (result.moveToNext());
            }
        }
        Collections.sort(conversations, CONVERSATION_COMPARATOR);
        return conversations;
    }

    public static List<Message> getSMSMessages(ContentResolver contentResolver, int threadId) {
        List<Message> messages = new ArrayList<>();
        try (Cursor result = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                Constants.PROJECTION_MESSAGE,
                Telephony.Sms.THREAD_ID+"=?",
                new String[]{String.valueOf(threadId)},
                null)) {
            if (result != null && result.moveToFirst()) {
                do {
                    Message message = new Message(
                            result.getInt(result.getColumnIndex(Constants.Columns._ID.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.THREAD_ID.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.TYPE.toString())),
                            result.getString(result.getColumnIndex(Constants.Columns.ADDRESS.toString())),
                            result.getString(result.getColumnIndex(Constants.Columns.BODY.toString())),
                            result.getLong(result.getColumnIndex(Constants.Columns.DATE.toString())),
                            result.getLong(result.getColumnIndex(Constants.Columns.DATE_SENT.toString())),
                            result.getInt(result.getColumnIndex(Constants.Columns.READ.toString())) == 1
                    );
                    Log.d("TAG", "date: " + message.date + " dateSent: " + message.dateSent);
                    messages.add(message);
                } while (result.moveToNext());
            }
        }
        Collections.sort(messages, MESSAGE_COMPARATOR);
        Message lastMessage = null;
        for (Message message : messages) {
            if (lastMessage != null) {
                int type = Utils.isSimilarType(lastMessage, message);
                switch (type) {
                    case Constants.MT_S:
                        if (new DateTime(lastMessage.dateSent).getMinuteOfDay() == new DateTime(message.dateSent).getMinuteOfDay())
                            message.type += 10;
                        break;
                    case Constants.MT_R:
                        if (new DateTime(lastMessage.date).getMinuteOfDay() == new DateTime(message.date).getMinuteOfDay())
                            message.type += 10;
                        break;
                }
            }
            lastMessage = message;
        }
        return messages;
    }

    public static List<Message> test(ContentResolver contentResolver) {
        Cursor result = contentResolver.query(Telephony.MmsSms.CONTENT_URI, null, null, null, null);
        if (result != null && result.moveToFirst()) {
            do {
                Log.e("next", "msg");
                String[] names = result.getColumnNames();
                for (String name : names) {
                    int index = result.getColumnIndex(name);
                    if (index > -1) {
                        Log.d(name, "" + result.getString(index));
                    }
                }
            } while(result.moveToNext());
            result.close();
        }
        return null;
    }
}
