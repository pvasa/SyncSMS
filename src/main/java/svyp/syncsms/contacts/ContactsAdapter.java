package svyp.syncsms.contacts;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import svyp.syncsms.R;
import svyp.syncsms.models.Contact;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView contactCardView;
        TextView tvName, tvNumber;

        ViewHolder(CardView v) {
            super(v);
            contactCardView = v;
            tvName = (TextView) v.findViewById(R.id.tv_message);
            tvNumber = (TextView) v.findViewById(R.id.tv_date);
        }
    }

    ContactsAdapter(List<Contact> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_contact, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDataset.get(position).name);
        holder.tvNumber.setText(mDataset.get(position).number);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
