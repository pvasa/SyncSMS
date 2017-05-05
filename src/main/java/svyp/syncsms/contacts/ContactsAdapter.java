package svyp.syncsms.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import svyp.syncsms.R;
import svyp.syncsms.models.Contact;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    private List<Contact> mDataset;

    ContactsAdapter(List<Contact> mDataset, Context context) {
        this.context = context;
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_contacts, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDataset.get(position).getName());
        holder.tvNumber.setText(mDataset.get(position).getNumber());
        Bitmap bitmap = loadContactPhoto(mDataset.get(position).getPhotoURI());
        if (bitmap != null) {
            holder.ivUserImage.setImageBitmap(bitmap);
        } else {
            holder.ivUserImage.setImageResource(R.drawable.ic_person_outline_black_24dp);
        }
    }

    public Bitmap loadContactPhoto(Uri imageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(List<Contact> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView contactCardView;
        TextView tvName, tvNumber;
        ImageView ivUserImage;

        ViewHolder(CardView v) {
            super(v);
            contactCardView = v;
            tvName = (TextView) v.findViewById(R.id.user_name);
            tvNumber = (TextView) v.findViewById(R.id.user_number);
            ivUserImage = (ImageView) v.findViewById(R.id.iv_user_picture);
        }
    }
}
