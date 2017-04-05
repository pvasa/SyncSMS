package svyp.syncsms.archived;

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

public class ArchivedFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TITLE = "Archived";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ArchivedFragment newInstance(int sectionNumber) {
        ArchivedFragment fragment = new ArchivedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_archived, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_archived);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ArchivedAdapter(new ArrayList<Message>());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                if (direction == ItemTouchHelper.LEFT) {
                    Message removedMessage = ((ArchivedAdapter) mAdapter).removeMessage(position);
                    ((MainActivity) getActivity()).unArchive(removedMessage);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    public void addMessage(Message message) {
        ((ArchivedAdapter) mAdapter).addMessage(message);
    }
}
