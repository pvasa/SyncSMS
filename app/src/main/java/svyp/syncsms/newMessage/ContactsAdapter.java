package svyp.syncsms.newMessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<String> dataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageCardView;
        TextView tvName;

        ViewHolder(TextView v) {
            super(v);
            messageCardView = v;
            tvName = (TextView) v.findViewById(R.id.tv_name);
        }
    }

    ContactsAdapter(ArrayList<String> dataset) {
        this.dataset = dataset;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_contacts, parent, false);
        return new ContactsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
