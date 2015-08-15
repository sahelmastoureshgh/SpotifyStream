package com.sahelmastoureshgh.spotifystream;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment {
    int selectedSongNumber;
    ArrayList<Song> songList;
    ArrayList<Song> songListTemp;
    Song currentSong;
    Song currentPlayedSong;
    PlayerService mPlayberService;
    TextView playerArtistName;
    TextView playerAlbumName;
    ImageView playerAlbumImage;
    TextView playerTrackName;
    ImageButton playerButton;
    ImageButton playerButtonPrevious;
    ImageButton playerButtonNext;
    SeekBar playerSeekBarPlayer;
    TextView playerSongDuration;
    TextView playerSongTime;
    private Handler handler = new Handler();
    SeekBarThread tempSeekBar;
    int songPosition=0;



    public PlayerActivityFragment() {
        tempSeekBar = new SeekBarThread();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentPlayedSong = new Song(null, null, null, null);
            songListTemp =new ArrayList<>();
            currentPlayedSong = savedInstanceState.getParcelable("currentPlayedSong");
            songPosition = savedInstanceState.getInt("songPosition", 0);
            songListTemp = savedInstanceState.getParcelableArrayList("songList");
            currentSong = currentPlayedSong;
            songList = songListTemp;

        }

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
        playerSongDuration = (TextView) rootView.findViewById(R.id.player_time_end);
        playerSongTime = (TextView) rootView.findViewById(R.id.player_time_start);
        playerButton.setTag(0);

        //Get date from intent which is songs list and its selected position in the list
        Intent intent = getActivity().getIntent();
        if (savedInstanceState == null && intent != null && intent.hasExtra(Intent.EXTRA_REFERRER_NAME) && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            songList = intent.getExtras().getParcelableArrayList(Intent.EXTRA_RESTRICTIONS_LIST);
            selectedSongNumber = intent.getIntExtra(Intent.EXTRA_REFERRER, 0);
            currentSong = songList.get(selectedSongNumber);


        }
        if (currentSong != null) {
            updateView(currentSong);

        }



        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buttonMode = (int) playerButton.getTag();
                if (buttonMode == 0) {
                    playerButton.setImageResource(android.R.drawable.ic_media_play);
                    mPlayberService.pause();
                    playerButton.setTag(1);
                } else {
                    playerButton.setImageResource(android.R.drawable.ic_media_pause);
                    mPlayberService.resume();
                    playerButton.setTag(0);
                }
            }
        });

        playerButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSongNumber = selectedSongNumber + 1;
                selectedSongNumber %= songList.size();
                currentSong = songList.get(selectedSongNumber);
                songPosition=0;
                updateView(currentSong);
                mPlayberService.Play(currentSong.getPreviewUrl());
            }
        });

        playerButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSongNumber = selectedSongNumber - 1;
                if (selectedSongNumber < 0)
                    selectedSongNumber = songList.size() - 1;
                currentSong = songList.get(selectedSongNumber);
                songPosition=0;
                updateView(currentSong);
                mPlayberService.Play(currentSong.getPreviewUrl());
            }
        });

        playerSeekBarPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int playerProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b &&  mPlayberService!=null)
                    mPlayberService.Seek(i);
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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(300, 500);
        return dialog;
    }

    /**
     * return formated date
     *
     * @param milliseconds
     * @return
     */
    public String formatMiliSecond(long milliseconds) {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }
    @Override
    public void onDestroyView() {
        handler.removeCallbacks(tempSeekBar);
        super.onDestroyView();
    }

    /**
     * update view and seek bar
     *
     * @param selectedSong
     */
    public void updateView(Song selectedSong) {
        playerAlbumName.setText(selectedSong.getAlbum());
        playerTrackName.setText(selectedSong.getName());
        Picasso.with(getActivity()).load(selectedSong.getPicture()).into(playerAlbumImage);
        playerSeekBarPlayer.setMax(30000);
        playerSeekBarPlayer.setProgress(songPosition);

    }
    private class SeekBarThread implements Runnable {

        @Override
        public void run() {
            if (mPlayberService != null) {
                long positionPlay = (long) mPlayberService.getCurrentSongPosition();
                songPosition=(int)positionPlay;
                playerSongTime.setText(formatMiliSecond(positionPlay));
                playerSeekBarPlayer.setProgress((int) positionPlay);
            }
            handler.postDelayed(this, 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Bind to Service
        Intent playerIntent = new Intent(getActivity(), PlayerService.class);
        //If it has not been bind already then bind it
        if (mPlayberService == null) {
            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, mConnection, Context.BIND_AUTO_CREATE);


        }
        if(tempSeekBar !=null)
        {
            playerSeekBarPlayer.post(tempSeekBar);
        }

    }

    @Override
    public void onStop() {

        // Unbind from the service
        getActivity().unbindService(mConnection);
        super.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("currentPlayedSong", currentSong);
        outState.putInt("songPosition", songPosition);
        outState.putParcelableArrayList("songList",songList);
        super.onSaveInstanceState(outState);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to Service
            PlayerService.MediaPlayerBinder binder = (PlayerService.MediaPlayerBinder) service;
            mPlayberService = binder.getService();
            if(songPosition==0)
               mPlayberService.Play(currentSong.getPreviewUrl());
            else
               mPlayberService.Seek(songPosition);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


}
