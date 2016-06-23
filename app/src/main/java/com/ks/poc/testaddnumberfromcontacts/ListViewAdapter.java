package com.ks.poc.testaddnumberfromcontacts;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krit on 6/23/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context mContext;
    private LayoutInflater inflater;
    private List<TrueContact> trueContactList = null;
    private int searchType;

    public ListViewAdapter(Context context, List<TrueContact> trueContactList, int searchType) {
        mContext = context;
        this.trueContactList = trueContactList;
        inflater = LayoutInflater.from(mContext);
        this.searchType = searchType;
    }

    public class ViewHolder {
        ImageView contactPhoto;
        TextView primaryText;
        TextView secondaryText;
        ImageButton addButton;
    }

    @Override
    public int getCount() {
        return trueContactList.size();
    }

    @Override
    public Object getItem(int i) {
        return trueContactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.primaryText = (TextView) view.findViewById(R.id.primary_text);
            holder.secondaryText = (TextView) view.findViewById(R.id.secondary_text);
            // Locate the ImageView in listview_item.xml
            holder.contactPhoto = (ImageView) view.findViewById(R.id.contactImage);
            holder.addButton = (ImageButton) view.findViewById(R.id.addButton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        if (searchType == 1) {
            // search by contact name
            holder.primaryText.setText(trueContactList.get(i).getContactName());
            holder.secondaryText.setText(trueContactList.get(i).getContactNumber());
        } else {
            // search by phone number
            holder.primaryText.setText(trueContactList.get(i).getContactNumber());
            holder.secondaryText.setText(trueContactList.get(i).getContactName());
        }
        String contactPhotoUri;
        contactPhotoUri = trueContactList.get(i).getContactImage();
        if (contactPhotoUri != null) {
            // Set the results into ImageView
            holder.contactPhoto.setImageURI(Uri.parse(contactPhotoUri));
        } else {
            holder.contactPhoto.setImageResource(R.drawable.contact_image);
        }
        holder.addButton.setImageResource(R.drawable.plus);
        return view;
    }
}
