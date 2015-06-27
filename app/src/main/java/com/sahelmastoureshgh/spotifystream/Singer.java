package com.sahelmastoureshgh.spotifystream;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahelmastoureshgh on 6/14/15.
 * Model for an Artist to keep name, picture and id of singer
 */
public class Singer implements Parcelable{
    private String name;
    private String picture;
    private String id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Creator<Singer> getCREATOR() {
        return CREATOR;
    }


    public Singer(String name, String picture, String id) {
        this.name = name;
        this.picture = picture;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(picture);
        dest.writeString(id);

    }
    public static final Creator<Singer> CREATOR
            = new Creator<Singer>() {
        public Singer createFromParcel(Parcel in) {
            return new Singer(in);
        }

        public Singer[] newArray(int size) {
            return new Singer[size];
        }
    };

    private Singer(Parcel p) {
        name = p.readString();
        picture = p.readString();
        id = p.readString();
    }
}
