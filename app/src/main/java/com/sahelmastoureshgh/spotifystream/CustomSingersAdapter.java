package com.sahelmastoureshgh.spotifystream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom Adapter for Singer
 * It will take data from a source
 * and use it to populate the ListView
 */
public class CustomSingersAdapter extends ArrayAdapter<Singer> {

    public CustomSingersAdapter(Context context, int resource, int textViewResourceId, List<Singer> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Singer mySinger = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_singer, parent, false);
        }
        // Lookup view for data population
        TextView singerName = (TextView) convertView.findViewById(R.id.list_item_singer_textview);
        ImageView singerPicture = (ImageView) convertView.findViewById(R.id.list_item_picture);

        // Populate the data into the template view using the data object for singers who have pictures and name
        if (mySinger.picture != null && mySinger.name != null) {
            singerName.setText(mySinger.name);
            Picasso.with(getContext()).load(mySinger.picture).fit()
                    .centerInside().into(singerPicture);
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
