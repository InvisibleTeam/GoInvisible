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
    private @Nullable String secondKey;
    private String value;
    private @Nullable String secondValue;
    private String formattedValue;
    private TagType tagType;

    public Tag(String key, String value, TagType tagType) {
        this(key, null, value, null, tagType);
    }

    public Tag(
            String key,
            @Nullable String secondKey,
            String value,
            @Nullable String secondValue,
            TagType tagType) {
        this.key = key;
        this.secondKey = secondKey;
        this.value = value;
        this.secondValue = secondValue;
        this.tagType = tagType;

        updateFormattedValue();
    }

    protected Tag(Parcel in) {
        this.key = in.readString();
        this.secondKey = in.readString();
        this.value = in.readString();
        this.secondValue = in.readString();
        this.formattedValue = in.readString();
        this.tagType = in.readParcelable(TagType.class.getClassLoader());
    }

    private void updateFormattedValue() {
        formattedValue = generateFormattedValue();
    }

    protected String generateFormattedValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    @Nullable
    public String getSecondKey() {
        return secondKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;

        updateFormattedValue();
    }

    @Nullable
    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(@Nullable String secondValue) {
        this.secondValue = secondValue;

        updateFormattedValue();
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
        dest.writeString(this.secondKey);
        dest.writeString(this.value);
        dest.writeString(this.secondValue);
        dest.writeString(this.formattedValue);
        dest.writeParcelable(this.tagType, flags);
    }
}
