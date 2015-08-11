package com.sahelmastoureshgh.spotifystream;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahelmastoureshgh on 6/24/15.
 * Model for an Track to keep name, album name and album's picture of song
 */
public class Song implements Parcelable {
    private String name;
    private String album;
    private String picture;
    private String previewUrl;


    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Song(String name, String album, String picture, String previewUrl) {
        this.name = name;
        this.album = album;
        this.picture = picture;
        this.previewUrl = previewUrl;

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
        dest.writeString(previewUrl);




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
        previewUrl = p.readString();

    }
}
