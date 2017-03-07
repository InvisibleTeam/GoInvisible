package com.invisibleteam.goinvisible.model;

import android.support.annotation.StringRes;

import com.invisibleteam.goinvisible.R;

public enum TagGroupType {

    IMAGE_INFO(R.string.tag_group_name_image_info),
    LOCATION_INFO(R.string.tag_group_name_location_info),
    DEVICE_INFO(R.string.tag_group_name_device_info),
    ADVANCED(R.string.tag_group_name_advanced);

    @StringRes private int groupNameResId;

    TagGroupType(int groupNameResId) {
        this.groupNameResId = groupNameResId;
    }

    public int getGroupNameResId() {
        return groupNameResId;
    }
}
