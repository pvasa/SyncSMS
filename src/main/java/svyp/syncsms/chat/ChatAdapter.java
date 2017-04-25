package svyp.syncsms.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Message;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View messageCardView;
        TextView mTVMessage, mTVDate;

        ViewHolder(View v) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        switch (viewType) {
            case Constants.MT_SENT:
            case Constants.MT_OUTBOX:
            case Constants.MT_FAILED:
            case Constants.MT_QUEUED:
                view = inflater.inflate(R.layout.ll_message_sent, parent, false);
                break;
            case Constants.MT_INBOX:
                view = inflater.inflate(R.layout.ll_message_received, parent, false);
                break;
            case Constants.MT_SENT_RECENT:
            case Constants.MT_OUTBOX_RECENT:
            case Constants.MT_FAILED_RECENT:
            case Constants.MT_QUEUED_RECENT:
                view = inflater.inflate(R.layout.ll_message_sent_recent, parent, false);
                break;
            case Constants.MT_INBOX_RECENT:
                view = inflater.inflate(R.layout.ll_message_received_recent, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mDataset.get(position);
        holder.mTVMessage.setText(message.body);
        switch (message.type) {
            case Constants.MT_SENT:
            case Constants.MT_OUTBOX:
            case Constants.MT_FAILED:
            case Constants.MT_QUEUED:
            case Constants.MT_INBOX:
                holder.mTVDate.setText(Utils.processDateTime(Math.max(message.date, message.dateSent)));
                break;
            /*case Constants.MT_SENT_RECENT:
            case Constants.MT_OUTBOX_RECENT:
            case Constants.MT_FAILED_RECENT:
            case Constants.MT_QUEUED_RECENT:
            case Constants.MT_INBOX_RECENT:
                holder.mTVDate.setVisibility(View.GONE);
                break;*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).type;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
