package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

    @SuppressWarnings("unused")
    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    private String key;
    private String value;
    private ObjectType objectType;

    public Tag(String key, String value, ObjectType objectType) {
        this.key = key;
        this.value = value;
        this.objectType = objectType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    protected Tag(Parcel in) {
        this(in.readString(), in.readString(), (ObjectType) in.readSerializable());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeSerializable(objectType);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
