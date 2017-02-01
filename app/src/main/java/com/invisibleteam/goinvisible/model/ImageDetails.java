package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageDetails implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ImageDetails> CREATOR = new Parcelable.Creator<ImageDetails>() {
        @Override
        public ImageDetails createFromParcel(Parcel in) {
            return new ImageDetails(in);
        }

        @Override
        public ImageDetails[] newArray(int size) {
            return new ImageDetails[size];
        }
    };

    private String path;
    private String name;
    private boolean isJpeg;

    public ImageDetails(String path, String name) {
        this.path = path;
        this.name = name;
        isJpeg = path.matches(".*jpg$");
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    protected ImageDetails(Parcel in) {
        path = in.readString();
        name = in.readString();
        isJpeg = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeByte((byte) (isJpeg ? 1 : 0));
    }

    public boolean isJpeg() {
        return isJpeg;
    }
}
