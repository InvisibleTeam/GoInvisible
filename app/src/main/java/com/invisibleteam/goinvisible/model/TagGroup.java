package com.invisibleteam.goinvisible.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class TagGroup implements Parent<Tag>, Parcelable {

    public static final Parcelable.Creator<TagGroup> CREATOR = new Parcelable.Creator<TagGroup>() {
        @Override
        public TagGroup createFromParcel(Parcel in) {
            return new TagGroup(in);
        }

        @Override
        public TagGroup[] newArray(int size) {
            return new TagGroup[size];
        }
    };

    private final TagGroupType type;
    private final List<Tag> tags;
    private final boolean initiallyExpanded;

    public TagGroup(TagGroupType type, List<Tag> tags, boolean initiallyExpanded) {
        this.type = type;
        this.tags = tags;
        this.initiallyExpanded = initiallyExpanded;
    }

    protected TagGroup(Parcel in) {
        int intTagGroupType = in.readInt();
        this.type = intTagGroupType == -1 ? null : TagGroupType.values()[intTagGroupType];
        if (in.readByte() == 0x01) {
            tags = new ArrayList<>();
            in.readList(tags, Tag.class.getClassLoader());
        } else {
            tags = null;
        }
        initiallyExpanded = in.readByte() != 0x00;
    }

    public TagGroupType getType() {
        return type;
    }

    @Override
    public List<Tag> getChildList() {
        return tags;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return initiallyExpanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeByte((byte) (initiallyExpanded ? 0x01 : 0x00));
    }
}
