package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.model.TagGroupType;

import java.util.ArrayList;
import java.util.List;

public class TagGroupUtil {

    private static final int IMAGE_INFO_SECTION_POSITION = 0;
    private static final int LOCATION_INFO_SECTION_POSITION = 21;
    private static final int DEVICE_INFO_SECTION_POSITION = 47;
    private static final int ADVANCED_INFO_SECTION_POSITION = 59;

    public static List<TagGroup> createTagGroups(List<Tag> tagList) {
        List<Tag> imageInfoSectionTagList = tagList.subList(IMAGE_INFO_SECTION_POSITION, LOCATION_INFO_SECTION_POSITION);
        List<Tag> locationInfoSectionTagList = tagList.subList(LOCATION_INFO_SECTION_POSITION, DEVICE_INFO_SECTION_POSITION);
        List<Tag> deviceInfoSectionTagList = tagList.subList(DEVICE_INFO_SECTION_POSITION, ADVANCED_INFO_SECTION_POSITION);
        List<Tag> advancedInfoSectionTagList = tagList.subList(ADVANCED_INFO_SECTION_POSITION, tagList.size());

        TagGroup imageInfoGroup = new TagGroup(TagGroupType.IMAGE_INFO, imageInfoSectionTagList, true);
        TagGroup locationInfoGroup = new TagGroup(TagGroupType.LOCATION_INFO, locationInfoSectionTagList, false);
        TagGroup deviceInfoGroup = new TagGroup(TagGroupType.DEVICE_INFO, deviceInfoSectionTagList, false);
        TagGroup advancedInfoGroup = new TagGroup(TagGroupType.ADVANCED, advancedInfoSectionTagList, false);

        List<TagGroup> tagGroups = new ArrayList<>();
        tagGroups.add(imageInfoGroup);
        tagGroups.add(locationInfoGroup);
        tagGroups.add(deviceInfoGroup);
        tagGroups.add(advancedInfoGroup);

        return tagGroups;
    }

    public static void updateTagList(List<TagGroup> tagGroupList, List<Tag> tagList) {
        List<Tag> imageInfoSectionTagList = tagList.subList(IMAGE_INFO_SECTION_POSITION, LOCATION_INFO_SECTION_POSITION);
        List<Tag> locationInfoSectionTagList = tagList.subList(LOCATION_INFO_SECTION_POSITION, DEVICE_INFO_SECTION_POSITION);
        List<Tag> deviceInfoSectionTagList = tagList.subList(DEVICE_INFO_SECTION_POSITION, ADVANCED_INFO_SECTION_POSITION);
        List<Tag> advancedInfoSectionTagList = tagList.subList(ADVANCED_INFO_SECTION_POSITION, tagList.size());

        TagGroup imageInfoGroup = new TagGroup(TagGroupType.IMAGE_INFO, imageInfoSectionTagList, true);
        TagGroup locationInfoGroup = new TagGroup(TagGroupType.LOCATION_INFO, locationInfoSectionTagList, false);
        TagGroup deviceInfoGroup = new TagGroup(TagGroupType.DEVICE_INFO, deviceInfoSectionTagList, false);
        TagGroup advancedInfoGroup = new TagGroup(TagGroupType.ADVANCED, advancedInfoSectionTagList, false);

        if (tagGroupList.size() > 0) {
            tagGroupList.set(0, imageInfoGroup);
        }
        if (tagGroupList.size() > 1) {
            tagGroupList.set(1, locationInfoGroup);
        }
        if (tagGroupList.size() > 2) {
            tagGroupList.set(2, deviceInfoGroup);
        }
        if (tagGroupList.size() > 3) {
            tagGroupList.set(3, advancedInfoGroup);
        }
    }

    public static List<Tag> deepCopyList(List<Tag> tagList) {
        List<Tag> newTagList = new ArrayList<>(tagList.size());
        for (int i = 0; i < tagList.size(); i++) {
            newTagList.add(tagList.get(i).copy());
        }
        return newTagList;
    }
}
