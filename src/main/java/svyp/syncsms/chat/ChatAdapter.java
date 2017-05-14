package svyp.syncsms.chat;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.Utils;
import svyp.syncsms.models.Message;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private SortedList<Message> mDataset;
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

    ChatAdapter(HashSet<Message> mDataset) {
        this.mDataset = new SortedList<>(Message.class, new MSortedListAdapterCallback(this));
        this.mDataset.addAll(mDataset);
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

    SortedList<Message> getDataset() {
        return mDataset;
    }

    void setDataset(SortedList<Message> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).type;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(Message message) {
        mDataset.add(message);
    }

    private class MSortedListAdapterCallback extends SortedListAdapterCallback<Message> {
        /**
         * Creates a {@link SortedList.Callback} that will forward data change events to the provided
         * Adapter.
         *
         * @param adapter The Adapter instance which should receive events from the SortedList.
         */
        MSortedListAdapterCallback(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Message o1, Message o2) {
            return o1.date > o2.date ? -1 : 1;
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Message oldItem, Message newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areItemsTheSame(Message item1, Message item2) {
            return item1._id == item2._id;
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
            onChanged(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
            onChanged(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    }
}
