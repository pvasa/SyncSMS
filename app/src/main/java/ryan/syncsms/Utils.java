package ryan.syncsms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;

public class Utils {

    public static void checkPermissions(String[] permissions, Activity activity, int requestCode) {
        ArraySet<String> unGranted = new ArraySet<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    explainPermission(permission, activity, requestCode);
                } else unGranted.add(permission);
            }
        }
        if (!unGranted.isEmpty()) {
            ActivityCompat.requestPermissions(activity, unGranted.toArray(new String[]{}), requestCode);
        }
    }

    private static void explainPermission(
            final String permission, final Activity activity, final int requestCode) {

        String title = "Permission required",
                positiveText = "Allow",
                negativeText = "Don't allow";
        DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        };
        DialogInterface.OnClickListener negativeAction = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        };

        switch (permission) {
            case Manifest.permission.SEND_SMS:
                new AlertDialog.Builder(activity)
                        .setTitle(title)
                        .setMessage("Please grant permission to send SMS for this app to function correctly.")
                        .setPositiveButton(positiveText, positiveAction)
                        .setNegativeButton(negativeText, negativeAction)
                        .setCancelable(false)
                        .show();
                break;
            default:
                new AlertDialog.Builder(activity)
                        .setTitle(title)
                        .setMessage("Please grant " + permission + " for this app to function correctly.")
                        .setPositiveButton(positiveText, positiveAction)
                        .setNegativeButton(negativeText, negativeAction)
                        .setCancelable(false)
                        .show();
        }
    }
}
