package svyp.syncsms.messages;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import svyp.syncsms.R;

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private String[] dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView mainCardView;
        TextView tv;
        ViewHolder(CardView v) {
            super(v);
            mainCardView = v;
            tv = (TextView) v.findViewById(R.id.tv_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MessagesAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_main, parent, false);
        // set the view's size, margins, padding and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv.setText(dataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
