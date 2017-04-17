package svyp.syncsms;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;

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

}
