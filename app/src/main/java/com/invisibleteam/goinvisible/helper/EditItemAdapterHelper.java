package com.invisibleteam.goinvisible.helper;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
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
            switch (tag.getTagGroupType()) {
                case IMAGE_INFO:
                    imageInfoTagList.add(tag);
                    break;
                case LOCATION_INFO:
                    locationInfoTagList.add(tag);
                    break;
                case DEVICE_INFO:
                    deviceInfoTagList.add(tag);
                    break;
                case ADVANCED:
                    advancedInfoTagList.add(tag);
                    break;
                default:
                    break;
            }
        }

        this.tagsList.clear();
        this.tagsList.addAll(imageInfoTagList);
        this.tagsList.addAll(locationInfoTagList);
        this.tagsList.addAll(deviceInfoTagList);
        this.tagsList.addAll(advancedInfoTagList);
    }

    private void addHeadersToLists() {
        imageInfoTagList.add(createTagWithGroupType(IMAGE_INFO));
        locationInfoTagList.add(createTagWithGroupType(LOCATION_INFO));
        deviceInfoTagList.add(createTagWithGroupType(DEVICE_INFO));
        advancedInfoTagList.add(createTagWithGroupType(ADVANCED));
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

    private Tag createTagWithGroupType(TagGroupType type) {
        return new Tag("", "", TagType.build(InputType.INDEFINITE), type);
    }
}
