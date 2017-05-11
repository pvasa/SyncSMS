package svyp.syncsms.contacts;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import svyp.syncsms.R;
import svyp.syncsms.models.Contact;

class SendToAdapter extends RecyclerView.Adapter<SendToAdapter.ViewHolder> {

    private ArrayList<Contact> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLLSentTo;
        TextView mTVName;

        ViewHolder(LinearLayout v) {
            super(v);
            mLLSentTo = v;
            mTVName = (TextView) v.findViewById(R.id.tv_name);
        }
    }

    SendToAdapter(ArrayList<Contact> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public SendToAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ll_send_to, parent, false);
        return new SendToAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SendToAdapter.ViewHolder holder, int position) {
        final Contact contact = mDataset.get(position);
        final int pos = position;
        holder.mTVName.setText(contact.getName());
        holder.mLLSentTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenu().add(Menu.NONE, pos, Menu.CATEGORY_CONTAINER, contact.getName());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        removeItem(item.getItemId());
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void addItem(Contact contact) {
        mDataset.add(contact);
        notifyItemInserted(mDataset.indexOf(contact));
        notifyItemRangeChanged(mDataset.indexOf(contact), getItemCount());
    }

    private void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
