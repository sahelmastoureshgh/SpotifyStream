package com.sahelmastoureshgh.spotifystream;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

public class PlayerService  extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{
    MediaPlayer mMediaPlayer = null;
    private final IBinder mMediaPlayerBinder = new MediaPlayerBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }
    public class MediaPlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMediaPlayerBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }
    @Override
    public void onDestroy() {
            mMediaPlayer.release();
            super.onDestroy();
    }
    public void Play(String preview_url) {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(preview_url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }
    public void resume() {
        mMediaPlayer.start();
    }
    public int getCurrentSongDuration(){
        return mMediaPlayer.getDuration();
    }

    public int getCurrentSongPosition() {
        return mMediaPlayer.getCurrentPosition();
    }
    public int getProgress() {
        int duration = getCurrentSongDuration();
        if(duration!=0) {
            return getCurrentSongPosition() / duration;
        }
        return 0;
    }
}
