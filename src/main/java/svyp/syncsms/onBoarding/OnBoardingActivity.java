package svyp.syncsms.onBoarding;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArraySet;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import svyp.syncsms.Constants;
import svyp.syncsms.MainActivity;
import svyp.syncsms.R;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class OnBoardingActivity extends AppCompatActivity {

    private SignInFragment signInFragment;
    private MakeDefaultFragment makeDefaultFragment;
    private PermissionsFragment permissionsFragment;

    private ArrayList<Fragment> fragments;

    private static final String TAG = OnBoardingActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * conversations for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private void handleIfFirstRun() {
        int vc = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(Constants.PREF_VERSION_CODE, Constants.VC_DOES_NOT_EXIST);
        switch (vc) {
            case Constants.VC_DOES_NOT_EXIST:
                break;
            case Constants.VC_1:
            case Constants.VC_2:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        fragments = new ArrayList<>();
        fragments.add(signInFragment = new SignInFragment());
        fragments.add(makeDefaultFragment = new MakeDefaultFragment());
        fragments.add(permissionsFragment = new PermissionsFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ((PageIndicatorView) findViewById(R.id.pageIndicatorView)).setViewPager(mViewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIfFirstRun();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getArguments().getCharSequence(Constants.KEY_TITLE);
        }
    }

    void gotoNextPage() {
        if (mViewPager.getCurrentItem() < mSectionsPagerAdapter.getCount() - 1)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    void gotoPreviousPage() {
        if (mViewPager.getCurrentItem() > 0)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
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
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
