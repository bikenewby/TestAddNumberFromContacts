package com.ks.poc.testaddnumberfromcontacts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

/**
 * Created by Krit on 6/23/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context mContext;
    private LayoutInflater inflater;
    private List<TrueContact> trueContactList = null;
    private int searchType;
    private String searchText;

    public ListViewAdapter(Context context, List<TrueContact> trueContactList, int searchType, String searchText) {
        mContext = context;
        this.trueContactList = trueContactList;
        inflater = LayoutInflater.from(mContext);
        this.searchType = searchType;
        this.searchText = searchText;
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
        Spannable text;
        if (searchType == 1) {
            // search by contact name
            text = highlightResult(searchText,trueContactList.get(i).getContactName());
            if (text != null) {
                holder.primaryText.setText(text,TextView.BufferType.SPANNABLE);
            } else {
                holder.primaryText.setText(trueContactList.get(i).getContactName());
            }
            holder.secondaryText.setText(trueContactList.get(i).getContactNumber());
        } else {
            // search by phone number
            text = highlightResult(searchText,trueContactList.get(i).getContactNumber());
            if (text != null) {
                holder.primaryText.setText(text,TextView.BufferType.SPANNABLE);
            } else {
                holder.primaryText.setText(trueContactList.get(i).getContactNumber());
            }
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

    private Spannable highlightResult (String searchString, String content) {
        String data = content.toLowerCase(Locale.getDefault());
        if (data.contains(searchString)) {
            int startPos = data.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(content);
            ColorStateList blueColor;
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, null, null);
            spanText.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new ForegroundColorSpan(Color.BLUE),startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanText;
        } else {
            return null;
        }
    }
}
