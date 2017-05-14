package svyp.syncsms.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import svyp.syncsms.R;
import svyp.syncsms.models.Contact;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> mDataset;
    private ArrayList<Contact> mFilteredDataset;

    ContactsAdapter(ArrayList<Contact> mDataset, Context context) {
        this.context = context;
        this.mDataset = new ArrayList<>(mDataset);
        this.mFilteredDataset = new ArrayList<>(this.mDataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = mFilteredDataset.get(position);
        holder.mTVName.setText(contact.name);
        if (!contact.numbers.isEmpty()) {
            holder.mRVNumbers.setAdapter(new NumbersAdapter(contact.numbers));
        }
        Bitmap bitmap = loadContactPhoto(contact.photoURI);
        if (bitmap != null) {
            holder.mTVIdentifier.setVisibility(View.GONE);
            holder.mIVUserImage.setImageDrawable(null);
            holder.mIVUserImage.setImageBitmap(bitmap);
        } else {
            holder.mTVIdentifier.setVisibility(View.VISIBLE);
            holder.mIVUserImage.setImageBitmap(null);
            holder.mIVUserImage.setImageDrawable(new ColorDrawable(
                    ContextCompat.getColor(holder.mIVUserImage.getContext(), R.color.colorPrimary)));
            holder.mTVIdentifier.setText(contact.name.substring(0, 1));
        }
    }

    private Bitmap loadContactPhoto(Uri imageUri) {
        if (imageUri == null) return null;
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException ignored) {}
        return null;
    }

    @Override
    public int getItemCount() {
        return mFilteredDataset.size();
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

    void filter(String constraint) {
        constraint = constraint.trim().toLowerCase();
        if (constraint.length() < 1) {
            mFilteredDataset = new ArrayList<>(mDataset);
        } else {
            mFilteredDataset.clear();
            for (Contact contact : mDataset) {
                if (contact.name.trim().toLowerCase().contains(constraint)
                        || contact.numbers.contains(constraint)) {
                    mFilteredDataset.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}
