package svyp.syncsms.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.TelephonyProvider;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Contact;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = ContactsActivity.class.getName();

    private RecyclerView mRVSendTo, mRVContacts;
    private RecyclerView.Adapter<SendToAdapter.ViewHolder> mAdapterSendTo;
    private ContactsAdapter mAdapterContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(Constants.KEY_TITLE));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRVSendTo = (RecyclerView) findViewById(R.id.rv_send_to);
        mRVSendTo.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRVSendTo.setLayoutManager(layoutManager);
        mAdapterSendTo = new SendToAdapter(new ArrayList<Contact>());
        mRVSendTo.setAdapter(mAdapterSendTo);

        mRVContacts = (RecyclerView) findViewById(R.id.rv_messages);
        mRVContacts.setHasFixedSize(true);
        mRVContacts.setLayoutManager(new LinearLayoutManager(this));

        if (Utils.checkPermissions(
                new String[] {
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                },
                this, Constants.RC_PERMISSIONS_NEW_MESSAGE_ACTIVITY)) {
            new LoadContacts().execute();
        }
    }

    private class LoadContacts extends AsyncTask<Void, Void, ArrayList<Contact>> {

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            return TelephonyProvider.getAllContacts(getContentResolver());
        }

        protected void onPostExecute(ArrayList<Contact> result) {
            super.onPostExecute(result);
            if (mRVContacts != null) {
                mAdapterContacts = new ContactsAdapter(result, ContactsActivity.this);
                mRVContacts.setAdapter(mAdapterContacts);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean show = super.onPrepareOptionsMenu(menu);

        MenuItem mSearchItem = menu.findItem(R.id.action_search);
        mSearchItem.expandActionView();
        SearchView mSearchView = (SearchView) mSearchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapterContacts.filter(newText);
                return true;
            }
        });
        return show;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.RC_PERMISSIONS_NEW_MESSAGE_ACTIVITY: {
                ArraySet<String> unGranted = new ArraySet<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        unGranted.add(permissions[i]);
                    }
                }
                if (!unGranted.isEmpty()) {
                    finish();
                } else {
                    new LoadContacts().execute();
                }
            }
        }
    }
}
