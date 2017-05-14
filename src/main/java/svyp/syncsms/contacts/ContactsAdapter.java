package svyp.syncsms.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
import svyp.syncsms.R;
import svyp.syncsms.models.Contact;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    private SortedList<Contact> mDataset;

    ContactsAdapter(HashSet<Contact> mDataset, Context context) {
        this.context = context;
        this.mDataset = new SortedList<>(Contact.class, new MSortedListAdapterCallback(this));
        this.mDataset.addAll(mDataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = mDataset.get(position);
        holder.mTVName.setText(contact.getName());
        if (!contact.getNumbers().isEmpty()) {
            holder.mRVNumbers.setAdapter(new NumbersAdapter(contact.getNumbers()));
        }
        Bitmap bitmap = loadContactPhoto(contact.getPhotoURI());
        if (bitmap != null) {
            holder.mTVIdentifier.setVisibility(View.GONE);
            holder.mIVUserImage.setImageDrawable(null);
            holder.mIVUserImage.setImageBitmap(bitmap);
        } else {
            holder.mTVIdentifier.setVisibility(View.VISIBLE);
            holder.mIVUserImage.setImageBitmap(null);
            holder.mIVUserImage.setImageDrawable(new ColorDrawable(
                    ContextCompat.getColor(holder.mIVUserImage.getContext(), R.color.colorPrimary)));
            holder.mTVIdentifier.setText(contact.getName().substring(0, 1));
        }
    }

    private Bitmap loadContactPhoto(Uri imageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException ignored) {}
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    void set(SortedList<Contact> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCVContact;
        TextView mTVName, mTVIdentifier;
        RecyclerView  mRVNumbers;
        CircleImageView mIVUserImage;

        ViewHolder(CardView v) {
            super(v);
            mCVContact = v;
            mTVName = (TextView) v.findViewById(R.id.tv_name);
            mRVNumbers = (RecyclerView) v.findViewById(R.id.rv_numbers);
            mRVNumbers.setHasFixedSize(false);
            mRVNumbers.setLayoutManager(new LinearLayoutManager(v.getContext()));
            mTVIdentifier = (TextView) v.findViewById(R.id.contact_identifier);
            mIVUserImage = (CircleImageView) v.findViewById(R.id.iv_user_picture);
        }
    }

    MSortedListAdapterCallback initializeSortedListAdapterCallback() {
        return new MSortedListAdapterCallback(this);
    }

    private class MSortedListAdapterCallback extends SortedListAdapterCallback<Contact> {
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
        public int compare(Contact o1, Contact o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Contact oldItem, Contact newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areItemsTheSame(Contact item1, Contact item2) {
            return item1 == item2;
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
