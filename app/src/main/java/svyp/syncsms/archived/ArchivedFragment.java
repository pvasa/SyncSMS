package svyp.syncsms.archived;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        Message newMessage = new Message("Ryan", "+16476189379", "Test message", "04/04/2017");
        Message newMessage1 = new Message("Priyank", "6476189379", "Hey there, what's up?", "04/04/2017");

        mAdapter = new ArchivedAdapter(new Message[]{newMessage, newMessage1});
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
