package svyp.syncsms.newMessage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArraySet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Message;

public class NewMessageActivity extends AppCompatActivity {

    private static final String TAG = NewMessageActivity.class.getName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText edNewMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(Constants.KEY_TITLE));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Utils.checkPermissions(
                new String[] {
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                },
                this, Constants.RC_PERMISSIONS_NEW_MESSAGE_ACTIVITY);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom) {
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        Message newMessage = new Message("Ryan", "+16476189379", "Hi", "03:00 PM");
        Message newMessage1 = new Message("Priyank", "6476189379", "Hey there, what's up?jsdkfhkusakdufkuhakufkurwehkiufnwekcubekuhfkuwekufbuywebkauhfukykufshekuyawukfkuwekh", "03:05 PM");
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(newMessage);
        messages.add(newMessage1);
        /*messages.add(newMessage);
        messages.add(newMessage1);
        messages.add(newMessage);
        messages.add(newMessage1);
        messages.add(newMessage);
        messages.add(newMessage1);
        messages.add(newMessage);
        messages.add(newMessage1);
        messages.add(newMessage);
        messages.add(newMessage1);
        messages.add(newMessage);*/

        mAdapter = new NewMessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);

        edNewMessage = (EditText) findViewById(R.id.ed_new_message);
        edNewMessage.requestFocus();

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        findViewById(R.id.fab_send_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage = edNewMessage.getText().toString();
                SmsManager sms = SmsManager.getDefault();
                if (newMessage.equals("")) {
                    Snackbar.make(v, "Message cannot be empty.", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (newMessage.length() > 160) {
                    sms.sendMultipartTextMessage(
                            "6476189379", null, sms.divideMessage(newMessage), null, null);
                } else {
                    sms.sendTextMessage("6476189379", null, newMessage, null, null);
                }
                edNewMessage.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_message, menu);
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
}
