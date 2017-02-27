package com.invisibleteam.goinvisible.helper;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

public class EditItemAdapterHelper {

    private List<Tag> imageInfoTagList;
    private List<Tag> locationInfoTagList;
    private List<Tag> deviceInfoTagList;
    private List<Tag> advancedInfoTagList;

    private List<Tag> tagsList;

    public EditItemAdapterHelper() {
        imageInfoTagList = new ArrayList<>();
        locationInfoTagList = new ArrayList<>();
        deviceInfoTagList = new ArrayList<>();
        advancedInfoTagList = new ArrayList<>();

        tagsList = new ArrayList<>();
    }

    public void createGroups(List<Tag> tags) {
        clearTagLists();
        addHeadersToLists();
        for (Tag tag : tags) {
            if (IMAGE_INFO.equals(tag.getTagGroupType())) {
                imageInfoTagList.add(tag);
            } else if (LOCATION_INFO.equals(tag.getTagGroupType())) {
                locationInfoTagList.add(tag);
            } else if (DEVICE_INFO.equals(tag.getTagGroupType())) {
                deviceInfoTagList.add(tag);
            } else {
                advancedInfoTagList.add(tag);
            }
        }

        this.tagsList.clear();
        this.tagsList.addAll(imageInfoTagList);
        this.tagsList.addAll(locationInfoTagList);
        this.tagsList.addAll(deviceInfoTagList);
        this.tagsList.addAll(advancedInfoTagList);
    }

    private void addHeadersToLists() {
        imageInfoTagList.add(new Tag("", "", TagType.build(InputType.INDEFINITE), IMAGE_INFO)); //todo generate method for this
        locationInfoTagList.add(new Tag("", "", TagType.build(InputType.INDEFINITE), LOCATION_INFO));
        deviceInfoTagList.add(new Tag("", "", TagType.build(InputType.INDEFINITE), DEVICE_INFO));
        advancedInfoTagList.add(new Tag("", "", TagType.build(InputType.INDEFINITE), ADVANCED));
    }

    private void clearTagLists() {
        imageInfoTagList.clear();
        locationInfoTagList.clear();
        deviceInfoTagList.clear();
        advancedInfoTagList.clear();
    }

    public List<Tag> getTagsList() {
        return tagsList;
    }
}
