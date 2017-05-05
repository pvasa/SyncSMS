package svyp.syncsms.chat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Locale;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.TelephonyProvider;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Message;

public class ChatActivity extends AppCompatActivity implements Filterable {

    private static final String TAG = ChatActivity.class.getName();

    private SearchView searchView;

    private TextAppearanceSpan highlightSpan;
    private TextAppearanceSpan normalSpan;

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private EditText edNewMessage;
    private Button btnSendMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(Constants.KEY_TITLE));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Utils.checkPermissions(
                new String[] {
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                },
                this, Constants.RC_PERMISSIONS_CONTACTS_ACTIVITY);

        ColorStateList colorStateListAccent =
                new ColorStateList(new int[][]{new int[]{}}, new int[] {
                        ContextCompat.getColor(this, R.color.colorAccent)
                });
        highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorStateListAccent, null);
        ColorStateList colorStateListPrimaryText =
                new ColorStateList(new int[][]{new int[]{}}, new int[] {
                        ContextCompat.getColor(this, R.color.colorPrimaryText)
                });
        normalSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, colorStateListPrimaryText, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_messages);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom) {
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mAdapter = new ChatAdapter(
                TelephonyProvider.getSMSMessages(
                        getContentResolver(), getIntent().getIntExtra(Constants.KEY_THREAD_ID, -1)));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        if (getIntent().getBooleanExtra(Constants.KEY_ARCHIVED, false)) {
            findViewById(R.id.rl_new_message).setVisibility(View.GONE);
        } else {
            edNewMessage = (EditText) findViewById(R.id.ed_new_message);
            edNewMessage.requestFocus();

            btnSendMessage = (Button) findViewById(R.id.btn_send_message);
            btnSendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newMessage = edNewMessage.getText().toString();
                    SmsManager smsManager = SmsManager.getDefault();
                    if (newMessage.length() > 160) {
                        smsManager.sendMultipartTextMessage(
                                "6476189379", null, smsManager.divideMessage(newMessage), null, null);
                    } else {
                        smsManager.sendTextMessage("6476189379", null, newMessage, null, null);
                    }
                    edNewMessage.setText("");
                }
            });
            btnSendMessage.setClickable(false);

            edNewMessage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count > 0) {
                        btnSendMessage.setClickable(true);
                    } else {
                        btnSendMessage.setClickable(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<Message> mDataset = mAdapter.getDataset();
                String[] constraintWords =
                        constraint.toString().trim().toLowerCase(Locale.CANADA).split(" ");

                for (int i = 0; i < mDataset.size(); i++) {

                    Message message = mDataset.get(i);
                    String bodyString = message.body.toString().trim().toLowerCase(Locale.CANADA);
                    int startPos, endPos;
                    boolean found = false;

                    for (String word : constraintWords) {
                        for (startPos = bodyString.indexOf(word);
                             startPos > -1;
                             startPos = bodyString.indexOf(word, startPos + 1)) {
                            endPos = startPos + word.length();
                            CharacterStyle characterStyle = CharacterStyle.wrap(highlightSpan);
                            message.spans.add(characterStyle);
                            message.body.setSpan(characterStyle,
                                    startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            found = true;
                        }
                    }
                    if (!found && !message.spans.isEmpty()) {
                        for (CharacterStyle span : message.spans)
                            message.body.removeSpan(span);
                        message.spans.clear();
                    }
                    mDataset.set(i, message);
                }
                FilterResults results = new FilterResults();
                results.values = mDataset;
                results.count = mDataset.size();
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mAdapter.setDataset((ArrayList<Message>) results.values);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean show = super.onPrepareOptionsMenu(menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        getFilter().filter(query);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        getFilter().filter(newText);
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
    public void onBackPressed() {
        if (searchView.isIconified()) super.onBackPressed();
        else searchView.onActionViewCollapsed();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.RC_PERMISSIONS_CONTACTS_ACTIVITY: {
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
