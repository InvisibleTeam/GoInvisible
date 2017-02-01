package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
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
    private TagType tagType;

    public Tag(String key, String value, TagType tagType) {
        this.key = key;
        this.value = value;
        this.tagType = tagType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public TagType getTagType(){
        return tagType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected Tag(Parcel in) {
        key = in.readString();
        value = in.readString();
        tagType = (TagType) in.readValue(TagType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeValue(tagType);
    }
}
