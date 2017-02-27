package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.databinding.SectionEditItemViewBinding;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.TextViewUtil;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {

    private static final int IMAGE_INFO_SECTION_POSITION = 0;
    private static final int LOCATION_INFO_SECTION_POSITION = 21;
    private static final int DEVICE_INFO_SECTION_POSITION = 47;
    private static final int ADVANCED_INFO_SECTION_POSITION = 59;
    private List<Tag> tagsList;
    private List<Tag> baseTagsList;
    private OnTagActionListener onTagActionListener;
    private Context context;

    private List<Tag> imageInfoTagList;
    private List<Tag> locationInfoTagList;
    private List<Tag> deviceInfoTagList;
    private List<Tag> advancedInfoTagList;

    EditItemAdapter() {
        tagsList = new ArrayList<>();
        imageInfoTagList = new ArrayList<>();
        locationInfoTagList = new ArrayList<>();
        deviceInfoTagList = new ArrayList<>();
        advancedInfoTagList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType != -1) {
            final View sectionItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_edit_item_view, parent, false);
            return new SectionViewHolder(sectionItemView);
        }

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, parent, false);
        return new TagViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tag tag = tagsList.get(position);
        holder.itemView.setTag(tag);

        if (holder instanceof SectionViewHolder) {
            final String sectionName = tag.getTagGroupTypeName(context);
            ((SectionViewHolder) holder).setSectionName(sectionName);
        } else {
            ((TagViewHolder) holder).editItemViewModel.setModel(tag);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == IMAGE_INFO_SECTION_POSITION) {
            return IMAGE_INFO.ordinal();
        } else if (position == LOCATION_INFO_SECTION_POSITION) {
            return LOCATION_INFO.ordinal();
        } else if (position == DEVICE_INFO_SECTION_POSITION) {
            return DEVICE_INFO.ordinal();
        } else if (position == ADVANCED_INFO_SECTION_POSITION) {
            return ADVANCED.ordinal();
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    void updateTagList(List<Tag> tags) {
        createGroups(tags);
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            updateBaseTagList(tags);
        } else {
            onTagActionListener.onTagsUpdated();
        }
    }

    private void createGroups(List<Tag> tags) {
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

    void updateBaseTagList(List<Tag> tags) {
        baseTagsList.clear();
        for (Tag tag : tags) {
            baseTagsList.add(new Tag(tag));
        }
    }

    List<Tag> getChangedTags() {
        List<Tag> changedTags = new ArrayList<>();
        for (Tag tag : tagsList) {
            for (Tag baseTag : baseTagsList) {
                if (isTagChanged(baseTag, tag)) {
                    changedTags.add(tag);
                    break;
                }
            }
        }
        return changedTags;
    }

    List<Tag> getAllTags() {
        return tagsList;
    }

    private boolean isTagChanged(Tag baseTag, Tag tag) {
        if (tag.getValue() == null) {
            return false;
        }

        if (tag.getKey().equals(baseTag.getKey())) {
            if (!tag.getValue().equals(baseTag.getValue())) {
                return true;
            }
        }

        return false;
    }

    void setOnTagActionListener(OnTagActionListener listener) {
        this.onTagActionListener = listener;
    }

    void updateTag(Tag tag) {
        if (!tagsList.isEmpty()) {
            for (int index = 0; index < tagsList.size(); index++) {
                if (tagsList.get(index).getKey().equals(tag.getKey())) {
                    tagsList.set(index, tag);
                    notifyItemChanged(index);
                    onTagActionListener.onTagsUpdated();
                    break;
                }
            }
        }
    }

    @VisibleForTesting
    List<Tag> getTagsList() {
        return tagsList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class TagViewHolder extends ViewHolder {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        EditItemViewModel editItemViewModel;

        EditItemViewBinding editItemViewBinding;

        TagViewHolder(View itemView) {
            super(itemView);

            editItemViewModel = new EditItemViewModel();
            editItemViewBinding = EditItemViewBinding.bind(itemView);
            editItemViewBinding.setViewModel(editItemViewModel);
            TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
            setupListeners();
        }

        private void setupListeners() {
            editItemViewBinding.clearButton.setOnClickListener(v -> {
                if (onTagActionListener != null) {
                    Tag tag = (Tag) itemView.getTag();
                    onTagActionListener.onClear(tag);
                }
            });

            itemView.setOnClickListener(view -> {
                if (onTagActionListener != null) {
                    Tag tag = (Tag) itemView.getTag();
                    onTagActionListener.onEditStarted(tag);
                }
            });
        }
    }

    class SectionViewHolder extends ViewHolder {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        SectionEditItemViewModel sectionEditItemViewModel;

        SectionEditItemViewBinding sectionEditItemViewBinding;

        SectionViewHolder(View itemView) {
            super(itemView);

            sectionEditItemViewModel = new SectionEditItemViewModel();
            sectionEditItemViewBinding = SectionEditItemViewBinding.bind(itemView);
            sectionEditItemViewBinding.setViewModel(sectionEditItemViewModel);
        }

        void setSectionName(String name) {
            sectionEditItemViewModel.setSectionName(name);
        }
    }
}
