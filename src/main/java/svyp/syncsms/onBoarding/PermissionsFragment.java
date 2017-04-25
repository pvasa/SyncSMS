package svyp.syncsms.onBoarding;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svyp.syncsms.Constants;
import svyp.syncsms.MainActivity;
import svyp.syncsms.R;
import svyp.syncsms.Utils;

public class PermissionsFragment extends Fragment implements View.OnClickListener {

    public static final String TITLE = "Request permissions";

    public PermissionsFragment() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Constants.KEY_TITLE, TITLE);
        setArguments(bundle);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_grant:
                if (Utils.checkPermissions(
                        new String[] {
                                Manifest.permission.READ_SMS,
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
