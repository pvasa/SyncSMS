package svyp.syncsms.messages;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import svyp.syncsms.R;
import svyp.syncsms.models.Message;

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private Message[] dataset;

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

    MessagesAdapter(Message[] dataset) {
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
        holder.tvName.setText(dataset[position].name);
        holder.tvMessage.setText(dataset[position].message);
        holder.tvDate.setText(dataset[position].date);
        holder.ivUserPicture.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
