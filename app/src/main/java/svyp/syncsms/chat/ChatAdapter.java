package svyp.syncsms.chat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.R;
import svyp.syncsms.models.Message;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Message> dataset;

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

    ChatAdapter(ArrayList<Message> dataset) {
        this.dataset = dataset;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMessage.setText(dataset.get(position).message);
        holder.tvDate.setText(dataset.get(position).date);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
