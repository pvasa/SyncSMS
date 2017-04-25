package svyp.syncsms.onBoarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.TelephonyProvider;

public class MakeDefaultFragment extends Fragment {

    public static final String TITLE = "Make default";

    public MakeDefaultFragment() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Constants.KEY_TITLE, TITLE);
        setArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_make_default, container, false);

        rootView.findViewById(R.id.btn_make_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyProvider.test(getActivity().getContentResolver());
            }
        });
        return rootView;
    }
}
