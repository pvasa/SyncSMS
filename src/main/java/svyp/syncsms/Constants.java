package svyp.syncsms;

import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Conversations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import svyp.syncsms.models.Contact;

public class Constants {
    public static final int RC_PERMISSIONS_CONTACTS_ACTIVITY = 0;
    public static final int RC_PERMISSIONS_ON_BOARDING_ACTIVITY = 1;
    public static final int RC_SIGN_IN = 2;

    public static final String KEY_TITLE = "title";
    public static final String KEY_ARCHIVED = "archived";
    public static final String KEY_THREAD_ID = Columns.THREAD_ID.toString();

    public static final String PREF_VERSION_CODE = "version_code";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_SIGNED_IN = "signed_in";

    public static final int VC_DOES_NOT_EXIST = -1;
    public static final int VC_1= 1;
    public static final int VC_2= 2;

    public static final int MT_ALL = Sms.MESSAGE_TYPE_ALL;
    public static final int MT_S = 19;
    public static final int MT_R = 18;

    public static final int MT_INBOX = Sms.MESSAGE_TYPE_INBOX;
    public static final int MT_SENT = Sms.MESSAGE_TYPE_SENT;
    public static final int MT_DRAFT = Sms.MESSAGE_TYPE_DRAFT;
    public static final int MT_OUTBOX = Sms.MESSAGE_TYPE_OUTBOX;
    public static final int MT_FAILED = Sms.MESSAGE_TYPE_FAILED;
    public static final int MT_QUEUED = Sms.MESSAGE_TYPE_QUEUED;

    public static final HashSet<Integer> GROUP_SENT = new HashSet<Integer>() {{
        add(Constants.MT_SENT);
        add(Constants.MT_OUTBOX);
        add(Constants.MT_FAILED);
        add(Constants.MT_QUEUED);
        add(Constants.MT_SENT_RECENT);
        add(Constants.MT_OUTBOX_RECENT);
        add(Constants.MT_FAILED_RECENT);
        add(Constants.MT_QUEUED_RECENT);
    }};

    public static final HashSet<Integer> GROUP_RECEIVED = new HashSet<Integer>() {{
        add(Constants.MT_INBOX);
        add(Constants.MT_INBOX_RECENT);
    }};

    public static final int MT_ALL_RECENT = Sms.MESSAGE_TYPE_ALL + 10;
    public static final int MT_S_RECENT = Sms.MESSAGE_TYPE_ALL + 10;
    public static final int MT_R_RECENT = Sms.MESSAGE_TYPE_ALL + 10;

    public static final int MT_INBOX_RECENT = Sms.MESSAGE_TYPE_INBOX + 10;
    public static final int MT_SENT_RECENT = Sms.MESSAGE_TYPE_SENT + 10;
    public static final int MT_DRAFT_RECENT = Sms.MESSAGE_TYPE_DRAFT + 10;
    public static final int MT_OUTBOX_RECENT = Sms.MESSAGE_TYPE_OUTBOX + 10;
    public static final int MT_FAILED_RECENT = Sms.MESSAGE_TYPE_FAILED + 10;
    public static final int MT_QUEUED_RECENT = Sms.MESSAGE_TYPE_QUEUED + 10;

    public enum Columns {

        _ID(Sms._ID),
        //TYPE_DISCRIMINATOR_COLUMN(MmsSms.TYPE_DISCRIMINATOR_COLUMN),

        THREAD_ID(Conversations.THREAD_ID),
        TYPE(Conversations.TYPE),
        ADDRESS(Conversations.ADDRESS),
        BODY(Conversations.BODY),
        DATE(Conversations.DATE),
        DATE_SENT(Conversations.DATE_SENT),
        READ(Conversations.READ);

        private String value;

        Columns(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static final String[] PROJECTION_CONVERSATION = new String[] {
            Sms.Conversations.THREAD_ID,
            Sms.Conversations.ADDRESS,
            Sms.Conversations.BODY,
            Sms.DATE,
            Sms.DATE_SENT,
            Sms.READ
    };

    public static final String[] PROJECTION_MESSAGE = new String[] {
            Sms._ID,
            Sms.THREAD_ID,
            Sms.ADDRESS,
            Sms.BODY,
            Sms.DATE,
            Sms.DATE_SENT,
            Sms.READ,
            Sms.TYPE
    };

    public static final List<Contact> CONTACTS = new ArrayList<Contact>() {{
        add(new Contact("Ryan", "6476666666"));
        add(new Contact("Priyank", "5195555555"));
    }};
}
