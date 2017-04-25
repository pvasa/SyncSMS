package svyp.syncsms;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import svyp.syncsms.chat.ChatActivity;
import svyp.syncsms.models.Conversation;

class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

    private List<Conversation> mDataset;
    private boolean archive;

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

    ConversationsAdapter(List<Conversation> mDataset, boolean archive) {
        this.mDataset = mDataset;
        this.archive = archive;
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
        /*for (int i = 0; i < conversation.contacts.size(); i++) {
            names.append(conversation.contacts.get(i).name);
            if (i < conversation.contacts.size() - 1) names.append(", ");
        }*/
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
                    //conversation.unreadCount = 0;
                    mDataset.set(pos, conversation);
                    notifyItemChanged(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                }
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(Constants.KEY_TITLE, names.toString());
                intent.putExtra(Constants.KEY_THREAD_ID, conversation.threadId);
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
        Conversation removed = mDataset.get(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        return removed;
    }

    void addConversation(Conversation conversation) {
        int index = (Collections.binarySearch(mDataset, conversation, TelephonyProvider.CONVERSATION_COMPARATOR) * -1) - 1;
        mDataset.add(index, conversation);
        notifyItemInserted(index);
        notifyItemRangeChanged(index, getItemCount());
    }
}
