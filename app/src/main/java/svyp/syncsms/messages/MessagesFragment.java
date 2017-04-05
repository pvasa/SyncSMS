package svyp.syncsms.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import svyp.syncsms.MainActivity;
import svyp.syncsms.R;
import svyp.syncsms.models.Message;

public class MessagesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TITLE = "Messages";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static MessagesFragment newInstance(int sectionNumber) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).appendToolBarTitle(" - " + TITLE);
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_messages);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Message newMessage = new Message("Ryan", "+16476189379", "Test message", "04/04/2017");
        Message newMessage1 = new Message("Priyank", "6476189379", "Hey there, what's up?", "04/04/2017");
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(newMessage);
        messages.add(newMessage1);

        mAdapter = new MessagesAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    Message removedMessage = ((MessagesAdapter) mAdapter).removeMessage(position);
                    ((MainActivity) getActivity()).archive(removedMessage);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    public void addMessage(Message message) {
        ((MessagesAdapter) mAdapter).addMessage(message);
    }
}
