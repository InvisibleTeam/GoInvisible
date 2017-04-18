package com.invisibleteam.goinvisible.mvvm.edition;


import android.databinding.ObservableArrayList;
import android.support.v7.util.DiffUtil;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditItemGroupAdapter;
import com.invisibleteam.goinvisible.util.TagsManager;
import com.invisibleteam.goinvisible.util.binding.ObservableString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewEditViewModel implements EditItemGroupAdapter.ItemActionListener {

    private static final int IMAGE_INFO_SECTION_POSITION = 0;
    private static final int LOCATION_INFO_SECTION_POSITION = 21;
    private static final int DEVICE_INFO_SECTION_POSITION = 47;
    private static final int ADVANCED_INFO_SECTION_POSITION = 59;

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");
    private final List<TagGroup> originalModelList;
    private final ObservableArrayList<TagGroup> modelList = new ObservableArrayList<>();
    private final ObservableTagGroupDiffResultModel diffList = new ObservableTagGroupDiffResultModel();
    private final TagsManager tagsManager;

    public NewEditViewModel(ImageDetails imageDetails, TagsManager tagsManager) {
        this.tagsManager = tagsManager;
        title.set(imageDetails.getName());
        imageUrl.set(imageDetails.getPath());

        List<TagGroup> tagGroups = createTagGroups(tagsManager.getAllTags());
        originalModelList = tagGroups;
        modelList.addAll(tagGroups);
    }

    private List<TagGroup> createTagGroups(List<Tag> tagList) {
        List<Tag> imageInfoSectionTagList = tagList.subList(IMAGE_INFO_SECTION_POSITION, LOCATION_INFO_SECTION_POSITION);
        List<Tag> locationInfoSectionTagList = tagList.subList(LOCATION_INFO_SECTION_POSITION, DEVICE_INFO_SECTION_POSITION);
        List<Tag> deviceInfoSectionTagList = tagList.subList(DEVICE_INFO_SECTION_POSITION, ADVANCED_INFO_SECTION_POSITION);
        List<Tag> advancedInfoSectionTagList = tagList.subList(ADVANCED_INFO_SECTION_POSITION, tagList.size());

        TagGroup imageInfoGroup = new TagGroup(TagGroupType.IMAGE_INFO, imageInfoSectionTagList);
        TagGroup locationInfoGroup = new TagGroup(TagGroupType.LOCATION_INFO, locationInfoSectionTagList);
        TagGroup deviceInfoGroup = new TagGroup(TagGroupType.DEVICE_INFO, deviceInfoSectionTagList);
        TagGroup advancedInfoGroup = new TagGroup(TagGroupType.ADVANCED, advancedInfoSectionTagList);

        List<TagGroup> tagGroups = new ArrayList<>();
        tagGroups.add(imageInfoGroup);
        tagGroups.add(locationInfoGroup);
        tagGroups.add(deviceInfoGroup);
        tagGroups.add(advancedInfoGroup);

        return tagGroups;
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    public ObservableArrayList<TagGroup> getModelList() {
        return modelList;
    }

    public ObservableTagGroupDiffResultModel getDiffList() {
        return diffList;
    }

    public EditItemGroupAdapter.ItemActionListener getItemActionListener() {
        return this;
    }

    @Override
    public void onItemClick(int parentPosition, int childPosition, Tag tag) {

    }

    @Override
    public void onItemClearClick(int parentPosition, int childPosition, Tag tag) {
        //TODO some kind of threaded micro service
        TagGroup tagGroup = modelList.get(parentPosition);
        ArrayList<Tag> copy = new ArrayList<>(tagGroup.getChildList());
        Collections.copy(copy, tagGroup.getChildList());

        Tag tagCopy = tag.copy();
        tagsManager.clearTag(tagCopy);
        tagGroup.getChildList().set(childPosition, tagCopy);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TagListDiffCallback(tagGroup.getChildList(), copy));
        diffList.clear();
        diffList.add(new TagDiffResultModel(parentPosition, diffResult));
    }

    public void onClearAllClick() {
        //TODO some kind of threaded micro service
        List<TagDiffResultModel> resultModels = new ArrayList<>(modelList.size());
        for (int i = 0; i < modelList.size(); i++) {
            TagGroup tagGroup = modelList.get(i);
            ArrayList<Tag> copy = new ArrayList<>(tagGroup.getChildList());
            Collections.copy(copy, tagGroup.getChildList());
            for (int j = 0; j < tagGroup.getChildList().size(); j++) {
                Tag tagCopy = tagGroup.getChildList().get(j).copy();
                tagGroup.getChildList().set(j, tagCopy);
                tagsManager.clearTag(tagCopy);
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TagListDiffCallback(tagGroup.getChildList(), copy));
            resultModels.add(new TagDiffResultModel(i, diffResult));
        }
        diffList.clear();
        diffList.addAll(resultModels);
    }
}
