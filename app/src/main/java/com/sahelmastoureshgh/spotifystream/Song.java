package com.sahelmastoureshgh.spotifystream;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahelmastoureshgh on 6/24/15.
 * Model for an Track to keep name, album name and album's picture of song
 */
public class Song implements Parcelable {
    public String name;
    public String album;
    public String picture;


    public Song(String name, String album, String picture) {
        this.name = name;
        this.album = album;
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(album);
        dest.writeString(picture);


    }
    public static final Creator<Song> CREATOR
            = new Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    private Song(Parcel p) {
        name = p.readString();
        album = p.readString();
        picture = p.readString();
    }
}
