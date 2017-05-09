package svyp.syncsms.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import svyp.syncsms.R;
import svyp.syncsms.customLayout.TransparentCircle;
import svyp.syncsms.models.Contact;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    private List<Contact> mDataset;

    ContactsAdapter(List<Contact> mDataset, Context context) {
        this.context = context;
        this.mDataset = mDataset;
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
            Log.d("DEBUG", contact.getNumbers().get(0));
            holder.mRVNumbers.setAdapter(new NumbersAdapter(contact.getNumbers()));
        }
        Bitmap bitmap = loadContactPhoto(contact.getPhotoURI());
        if (bitmap != null) {
            holder.mTCUserIdentifier.setVisibility(View.GONE);
            holder.mIVUserImage.setVisibility(View.VISIBLE);
            holder.mIVUserImage.setImageBitmap(bitmap);
        } else {
            holder.mTCUserIdentifier.setVisibility(View.VISIBLE);
            holder.mIVUserImage.setVisibility(View.GONE);
            holder.mTVIdentifier.setText("A");
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

    void updateList(List<Contact> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCVContact;
        TextView mTVName, mTVIdentifier;
        RecyclerView  mRVNumbers;
        ImageView mIVUserImage;
        TransparentCircle mTCUserIdentifier;

        ViewHolder(CardView v) {
            super(v);
            mCVContact = v;
            mTVName = (TextView) v.findViewById(R.id.tv_name);
            mRVNumbers = (RecyclerView) v.findViewById(R.id.rv_numbers);
            mRVNumbers.setHasFixedSize(true);
            mRVNumbers.setLayoutManager(new LinearLayoutManager(v.getContext()));
            mTVIdentifier = (TextView) v.findViewById(R.id.contact_identifier);
            mIVUserImage = (ImageView) v.findViewById(R.id.iv_user_picture);
            mTCUserIdentifier = (TransparentCircle) v.findViewById(R.id.iv_user_identifier);
        }
    }
}
