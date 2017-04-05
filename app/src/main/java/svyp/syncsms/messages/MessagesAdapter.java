package svyp.syncsms.messages;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.R;
import svyp.syncsms.models.Message;

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private ArrayList<Message> dataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvMessage, tvDate;
        ImageView ivUserPicture;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvMessage = (TextView) v.findViewById(R.id.tv_message);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            ivUserPicture = (ImageView) v.findViewById(R.id.iv_user_picture);
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
        holder.tvName.setText(dataset.get(position).name);
        holder.tvMessage.setText(dataset.get(position).message);
        holder.tvDate.setText(dataset.get(position).date);
        holder.ivUserPicture.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        if (dataset != null) return dataset.size();
        return 0;
    }

    Message removeMessage(int position) {
        Message removed = dataset.get(position);
        dataset.remove(position);
        notifyItemRemoved(position);
        return removed;
    }

    void addMessage(Message message) {
        dataset.add(message);
        notifyItemInserted(dataset.indexOf(message));
    }
}
