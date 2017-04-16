package svyp.syncsms.onBoarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.Utils;

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static final String TITLE = "Introduction";

    private Button btnSignIn;

    public SignInFragment() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Constants.KEY_TITLE, TITLE);
        setArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        btnSignIn = (Button) rootView.findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);

        if (Utils.isSignedIn(getActivity())) btnSignIn.setVisibility(View.INVISIBLE);
        else btnSignIn.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                ((OnBoardingActivity) getActivity()).signIn();
                break;
        }
    }

    void signedIn(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }
}
