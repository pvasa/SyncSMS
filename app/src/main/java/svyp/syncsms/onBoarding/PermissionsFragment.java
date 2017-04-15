package svyp.syncsms.onBoarding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svyp.syncsms.Constants;
import svyp.syncsms.MainActivity;
import svyp.syncsms.R;
import svyp.syncsms.Utils;

public class PermissionsFragment extends Fragment
        implements View.OnClickListener {

    private final String TAG = getClass().getName();

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TITLE = "Request permissions";

    public static PermissionsFragment newInstance(int sectionNumber) {
        PermissionsFragment fragment = new PermissionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_permissions, container, false);

        rootView.findViewById(R.id.btn_grant).setOnClickListener(this);
        rootView.findViewById(R.id.btn_exit).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.RC_PERMISSIONS_ON_BOARDING_ACTIVITY: {
                ArraySet<String> unGranted = new ArraySet<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        unGranted.add(permissions[i]);
                    }
                }
                if (unGranted.isEmpty()) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_grant:
                if (Utils.checkPermissions(
                        new String[] {
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_CONTACTS
                        },
                        getActivity(), Constants.RC_PERMISSIONS_ON_BOARDING_ACTIVITY)) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.btn_exit:
                getActivity().finish();
                break;
        }
    }
}