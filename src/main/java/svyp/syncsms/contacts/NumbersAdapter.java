package svyp.syncsms.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.R;

class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    NumbersAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTVNumber;

        ViewHolder(LinearLayout v) {
            super(v);
            mTVNumber = (TextView) v.findViewById(R.id.tv_number);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_numbers, parent, false);
        return new NumbersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTVNumber.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
