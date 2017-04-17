package svyp.syncsms.chat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import svyp.syncsms.R;
import svyp.syncsms.models.Message;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView messageCardView;
        TextView mTVMessage, mTVDate;

        ViewHolder(CardView v) {
            super(v);
            messageCardView = v;
            mTVMessage = (TextView) v.findViewById(R.id.tv_message);
            mTVDate = (TextView) v.findViewById(R.id.tv_date);
        }
    }

    ChatAdapter(List<Message> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTVMessage.setText(mDataset.get(position).message);
        holder.mTVDate.setText(mDataset.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
