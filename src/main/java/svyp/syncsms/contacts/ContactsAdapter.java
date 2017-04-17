package svyp.syncsms.contacts;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import svyp.syncsms.R;
import svyp.syncsms.models.Message;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Message> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView messageCardView;
        TextView tvMessage, tvDate;

        ViewHolder(CardView v) {
            super(v);
            messageCardView = v;
            tvMessage = (TextView) v.findViewById(R.id.tv_message);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
        }
    }

    ContactsAdapter(List<Message> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMessage.setText(mDataset.get(position).message);
        holder.tvDate.setText(mDataset.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
