package svyp.syncsms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import svyp.syncsms.chat.ChatActivity;
import svyp.syncsms.models.Contact;
import svyp.syncsms.models.Conversation;

class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

    private SortedList<Conversation> mDataset;
    private boolean archive;
    private Context context;
    private ArrayList<Contact> contacts;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvMessage, tvDate, tvUnreadCount;
        ImageView ivUserPicture;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
            ivUserPicture = (ImageView) v.findViewById(R.id.iv_user_picture);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvMessage = (TextView) v.findViewById(R.id.tv_message);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            tvUnreadCount = (TextView) v.findViewById(R.id.tv_unread_count);
        }
    }

    ConversationsAdapter(HashSet<Conversation> mDataset, Context context, boolean archive) {
        this.archive = archive;
        this.mDataset = new SortedList<>(Conversation.class, new MSortedListAdapterCallback(this));
        this.mDataset.addAll(mDataset);
        this.context = context;
        this.contacts = TelephonyProvider.getAllContacts(context.getContentResolver());
    }

    @Override
    public ConversationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_conversation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Conversation conversation = mDataset.get(position);
        final int pos = holder.getAdapterPosition();

        holder.ivUserPicture.setImageResource(R.mipmap.ic_launcher);
        final StringBuilder names = new StringBuilder();
        names.append(conversation.address);
        holder.tvName.setText(conversation.address);
        holder.tvMessage.setText(conversation.snippet);
        holder.tvDate.setText(Utils.processDateTime(conversation.lastMessageDate));

        if (conversation.unreadCount > 0) {
            holder.tvUnreadCount.setText(String.valueOf(conversation.unreadCount));
        } else {holder.tvUnreadCount.setVisibility(View.GONE);}

        if (!conversation.read) {
            holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tvMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tvDate.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tvName.setTypeface(Typeface.DEFAULT);
            holder.tvMessage.setTypeface(Typeface.DEFAULT);
            holder.tvDate.setTypeface(Typeface.DEFAULT);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!conversation.read) {
                    conversation.read = true;
                    conversation.unreadCount = 0;
                    mDataset.updateItemAt(pos, conversation);
                }
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(Constants.KEY_TITLE, names.toString());
                intent.putExtra(Constants.KEY_THREAD_ID, conversation.threadId);
                intent.putExtra(Constants.KEY_ADDRESS, conversation.address);
                intent.putExtra(Constants.KEY_ARCHIVED, archive);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    Conversation removeConversation(int position) {
        return mDataset.removeItemAt(position);
    }

    void addConversation(Conversation conversation) {
        mDataset.add(conversation);
    }

    void addConversation(HashSet<Conversation> conversations) {
        mDataset.addAll(conversations);
    }

    private class MSortedListAdapterCallback extends SortedListAdapterCallback<Conversation> {
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
        public int compare(Conversation o1, Conversation o2) {
            return o1.lastMessageDate > o2.lastMessageDate ? -1 : 1;
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Conversation oldItem, Conversation newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areItemsTheSame(Conversation item1, Conversation item2) {
            return item1.threadId == item2.threadId;
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
