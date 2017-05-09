package svyp.syncsms.contacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.TelephonyProvider;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Contact;

public class ContactsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ContactsActivity.class.getName();

    private RecyclerView mRVSendTo, mRVContacts;
    private RecyclerView.Adapter mAdapterSendTo;
    private ContactsAdapter mAdapterContacts;
    private ArrayList<Contact> contacts;

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
        mAdapterSendTo = new SendToAdapter(Constants.CONTACTS);
        mRVSendTo.setAdapter(mAdapterSendTo);

        mRVContacts = (RecyclerView) findViewById(R.id.rv_messages);
        mRVContacts.setHasFixedSize(true);
        mRVContacts.setLayoutManager(new LinearLayoutManager(this));

        checkPermission();
    }

    private void checkPermission() {
        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            new LoadContacts().execute();
        } else {
            Utils.checkPermissions(
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CONTACTS
                    },
                    this, Constants.RC_PERMISSIONS_NEW_MESSAGE_ACTIVITY);
        }
    }

    private class LoadContacts extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            contacts = TelephonyProvider.getAllContacts(getContentResolver());
            return !contacts.isEmpty();
        }

        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            if(result && mRVContacts!=null){
                mAdapterContacts = new ContactsAdapter(contacts, ContactsActivity.this);
                mRVContacts.setAdapter(mAdapterContacts);
                mRVContacts.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        mRVContacts.scrollToPosition(mAdapterContacts.getItemCount() - 1);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        menu.findItem(R.id.action_search).expandActionView();

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
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
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "onQueryTextSubmit >> "+s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d(TAG, "onQueryTextChange >> "+s);
        filter(s);
        return false;
    }

    void filter(String text){
        List<Contact> temp = new ArrayList();
        for(Contact d: contacts){
            if(d.getName().contains(text) || d.getNumber().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        mAdapterContacts.updateList(temp);
    }
}
