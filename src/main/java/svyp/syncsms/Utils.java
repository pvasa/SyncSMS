package svyp.syncsms;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.telephony.TelephonyManager;
import android.view.View;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import svyp.syncsms.models.Message;

public class Utils {

    public static boolean checkPermissions(String[] permissions, Activity activity, int requestCode) {
        ArraySet<String> unGranted = new ArraySet<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_DENIED) {
                unGranted.add(permission);
            }
        }
        if (!unGranted.isEmpty()) {
            ActivityCompat.requestPermissions(activity, unGranted.toArray(new String[]{}), requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static void putStringPreference(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static void putIntPreference(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static void putBoolPreference(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static boolean isSignedIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PREF_SIGNED_IN, false);
    }

    public static String processDateTime(long milliseconds) {

        DateTime dtCurrent = DateTime.now();
        DateTime dtOfMessage = new DateTime(milliseconds);

        SimpleDateFormat sdf;
        if (dtOfMessage.getYear() != dtCurrent.getYear()) { // older than an year - include year
            sdf = new SimpleDateFormat("yyyy, MMM dd, HH:mm", Locale.CANADA);
        } else if (dtOfMessage.getWeekOfWeekyear() != dtCurrent.getWeekOfWeekyear()) { // older than a week/month - include month
            sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.CANADA);
        } else if (dtOfMessage.getDayOfWeek() != dtCurrent.getDayOfWeek()) { // older than a day - include weekday
            sdf = new SimpleDateFormat("E, HH:mm", Locale.CANADA);
        } else { // less than a day
            sdf = new SimpleDateFormat("HH:mm", Locale.CANADA);
        }

        return sdf.format(new Date(milliseconds));
    }

    public static int isSimilarType(Message m1, Message m2) {
        if (Constants.GROUP_SENT.contains(m1.type) && Constants.GROUP_SENT.contains(m2.type))
            return Constants.MT_S;
        else if (Constants.GROUP_RECEIVED.contains(m1.type) && Constants.GROUP_RECEIVED.contains(m2.type))
            return Constants.MT_R;
        else return -1;
    }

    public static boolean checkSIM(View v) {
        Context context = v.getContext();
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                Snackbar.make(v, R.string.snack_sim_absent, Snackbar.LENGTH_SHORT).show();
                return false;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                Snackbar.make(v, R.string.snack_sim_network_locked, Snackbar.LENGTH_SHORT).show();
                return false;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                Snackbar.make(v, R.string.snack_sim_pin_required, Snackbar.LENGTH_SHORT).show();
                return false;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                Snackbar.make(v, R.string.snack_sim_puk_required, Snackbar.LENGTH_SHORT).show();
                return false;
            case TelephonyManager.SIM_STATE_READY:
                return true;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                Snackbar.make(v, R.string.snack_sim_unknown, Snackbar.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }
}
