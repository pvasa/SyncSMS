package svyp.syncsms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svyp.syncsms.models.Conversation;

public class ConversationsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TITLE = "Messages";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ConversationsFragment newInstance(int sectionNumber) {
        ConversationsFragment fragment = new ConversationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).appendToolBarTitle(" - " + TITLE);
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_messages);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationsAdapter(
                TelephonyProvider.getAllConversations(getActivity().getContentResolver()), false);
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
                    Conversation conversation = ((ConversationsAdapter) mAdapter).removeConversation(position);
                    ((MainActivity) getActivity()).archive(conversation);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    public void addConversation(Conversation conversation) {
        ((ConversationsAdapter) mAdapter).addConversation(conversation);
    }
}
