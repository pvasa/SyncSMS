package svyp.syncsms;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;

public class Utils {

    public static void checkPermissions(String[] permissions, Activity activity, int requestCode) {
        ArraySet<String> unGranted = new ArraySet<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_DENIED) {
                unGranted.add(permission);
            }
        }
        if (!unGranted.isEmpty()) {
            ActivityCompat.requestPermissions(activity, unGranted.toArray(new String[]{}), requestCode);
        }
    }
}
