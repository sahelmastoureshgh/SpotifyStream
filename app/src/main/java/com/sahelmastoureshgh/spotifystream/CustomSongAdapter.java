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
 * Custom Adapter for Tracks
 * It will take data from a source
 * and use it to populate the ListView
 */
public class CustomSongAdapter extends ArrayAdapter<Song> {
    public CustomSongAdapter(Context context, int resource, int textViewResourceId, List<Song> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song currentSong = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song_track, parent, false);
        }
        // Lookup view for data population
        TextView  songName = (TextView) convertView.findViewById(R.id.list_item_song_textview);
        TextView  albumName = (TextView) convertView.findViewById(R.id.list_item_album_textview);
        ImageView songImage = (ImageView) convertView.findViewById(R.id.list_item_song_picture);

        // Populate the data into the template view using the data object for songs which have pictures and name and album name
        if (currentSong.picture != null && currentSong.name != null &&  currentSong.album != null) {
            songName.setText(currentSong.name);
            albumName.setText(currentSong.album);
            Picasso.with(getContext()).load(currentSong.picture).fit()
                    .centerInside().into(songImage);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
