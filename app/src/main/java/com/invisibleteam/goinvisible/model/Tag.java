package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nullable;

public class Tag implements Parcelable {

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    private String key;
    private @Nullable String value;
    protected String formattedValue;
    private TagType tagType;

    public Tag(
            String key,
            @Nullable String value,
            TagType tagType) {
        this.key = key;
        this.value = value;
        this.tagType = tagType;
        this.formattedValue = value;
    }

    public Tag(Tag tag) {
        this.key = tag.getKey();
        this.value = tag.getValue();
        this.tagType = tag.getTagType();
        this.formattedValue = tag.getValue();
    }

    protected Tag(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
        this.formattedValue = in.readString();
        this.tagType = in.readParcelable(TagType.class.getClassLoader());
    }

    public String getKey() {
        return key;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.formattedValue = value;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public TagType getTagType() {
        return tagType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeString(this.formattedValue);
        dest.writeParcelable(this.tagType, flags);
    }
}
