package com.sahelmastoureshgh.spotifystream;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment {
    Song selectedSong;

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        TextView playerArtistName = (TextView) rootView.findViewById(R.id.player_artist_name);
        TextView playerAlbumName = (TextView) rootView.findViewById(R.id.player_album_name);
        ImageView playerAlbumImage = (ImageView) rootView.findViewById(R.id.player_album_image);
        TextView playerTrackName = (TextView) rootView.findViewById(R.id.player_track_name);

        /* Get an Intent instance and retrieve data from the track */
        Intent intent = getActivity().getIntent();
        if (savedInstanceState==null && intent != null && intent.hasExtra(Intent.EXTRA_REFERRER_NAME) && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            String artistName = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
            playerArtistName.setText(artistName);
            selectedSong = intent.getExtras().getParcelable(Intent.EXTRA_REFERRER);
            playerAlbumName.setText(selectedSong.getAlbum());
            playerTrackName.setText(selectedSong.getName());
            Picasso.with(getActivity()).load(selectedSong.getPicture()).into(playerAlbumImage);


        }
        return rootView;
    }
}
