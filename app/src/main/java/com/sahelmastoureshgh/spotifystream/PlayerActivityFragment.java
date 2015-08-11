package com.sahelmastoureshgh.spotifystream;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment {
    int selectedSongNumber;
    ArrayList<Song> songList;
    Song currentSong;
    PlayerService mPlayberService;
    TextView playerArtistName;
    TextView playerAlbumName;
    ImageView playerAlbumImage;
    TextView playerTrackName;
    ImageButton playerButton;
    ImageButton playerButtonPrevious;
    ImageButton playerButtonNext;
    SeekBar playerSeekBarPlayer;


    public PlayerActivityFragment() {
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        playerArtistName = (TextView) rootView.findViewById(R.id.player_artist_name);
        playerAlbumName = (TextView) rootView.findViewById(R.id.player_album_name);
        playerAlbumImage = (ImageView) rootView.findViewById(R.id.player_album_image);
        playerTrackName = (TextView) rootView.findViewById(R.id.player_track_name);
        playerButton = (ImageButton) rootView.findViewById(R.id.play_track);
        playerButtonPrevious = (ImageButton) rootView.findViewById((R.id.play_previous_track));
        playerButtonNext = (ImageButton) rootView.findViewById(R.id.play_next_track);
        playerSeekBarPlayer = (SeekBar) rootView.findViewById(R.id.player_seek_bar);
        playerButton.setTag(0);

        Intent intent = getActivity().getIntent();
        if (savedInstanceState==null && intent != null && intent.hasExtra(Intent.EXTRA_REFERRER_NAME) && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            songList = intent.getExtras().getParcelableArrayList(Intent.EXTRA_RESTRICTIONS_LIST);
            selectedSongNumber = intent.getIntExtra(Intent.EXTRA_REFERRER, 0);
            currentSong = songList.get(selectedSongNumber);
            intialView(currentSong);

        }

            playerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonMode = (int)playerButton.getTag();
                    if (buttonMode == 0) {
                        playerButton.setImageResource(android.R.drawable.ic_media_pause);
                        mPlayberService.pause();
                        playerButton.setTag(1);
                    }
                    else {
                        playerButton.setImageResource(android.R.drawable.ic_media_play);
                        mPlayberService.resume();
                        playerButton.setTag(0);
                    }
                }
            });

            playerButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedSongNumber = selectedSongNumber+1;
                    selectedSongNumber %= songList.size();
                    currentSong = songList.get(selectedSongNumber);
                    intialView(currentSong);
                    mPlayberService.Play(currentSong.getPreviewUrl());
                }
            });

            playerButtonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedSongNumber = selectedSongNumber-1;
                    if(selectedSongNumber<0)
                        selectedSongNumber= songList.size()-1;
                    currentSong = songList.get(selectedSongNumber);
                    intialView(currentSong);
                    mPlayberService.Play(currentSong.getPreviewUrl());
                }
            });

            playerSeekBarPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int playerProgress;
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


        return rootView;
    }
    public void intialView(Song selectedSong) {
        playerAlbumName.setText(selectedSong.getAlbum());
        playerTrackName.setText(selectedSong.getName());
        Picasso.with(getActivity()).load(selectedSong.getPicture()).into(playerAlbumImage);
        playerSeekBarPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPlayberService != null) {
                    int progressPosition = mPlayberService.getCurrentSongPosition();

                    playerSeekBarPlayer.setProgress(progressPosition);

                }
            }
        }, 1000);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent playerIntent = new Intent(getActivity(), PlayerService.class);
        //If it has not been bind already then bind it
        if(mPlayberService == null){
            getActivity().bindService(playerIntent, mConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playerIntent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        getActivity().unbindService(mConnection);

    }
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to Service
            PlayerService.MediaPlayerBinder binder = (PlayerService.MediaPlayerBinder) service;
            mPlayberService = binder.getService();
            mPlayberService.Play(currentSong.getPreviewUrl());

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

}
