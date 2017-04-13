package svyp.syncsms.messages;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.Constants;
import svyp.syncsms.R;
import svyp.syncsms.chat.ChatActivity;
import svyp.syncsms.models.Message;

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private ArrayList<Message> dataset;

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

    MessagesAdapter(ArrayList<Message> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_messages, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Message message = dataset.get(position);
        final int pos = holder.getAdapterPosition();

        holder.ivUserPicture.setImageResource(R.mipmap.ic_launcher);
        holder.tvName.setText(message.name);
        holder.tvMessage.setText(message.message);
        holder.tvDate.setText(message.date);

        if (message.unreadCount > 0) {
            holder.tvUnreadCount.setText(String.valueOf(message.unreadCount));
        } else {holder.tvUnreadCount.setVisibility(View.GONE);}

        if (!message.read) {
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
                if (!message.read) {
                    message.read = true;
                    message.unreadCount = 0;
                    dataset.set(pos, message);
                    notifyItemChanged(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                }
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(Constants.KEY_TITLE, message.name);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }

    Message removeMessage(int position) {
        Message removed = dataset.get(position);
        dataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        return removed;
    }

    void addMessage(Message message) {
        dataset.add(message);
        notifyItemInserted(dataset.indexOf(message));
        notifyItemRangeChanged(dataset.indexOf(message), getItemCount());
    }
}
