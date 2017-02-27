package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.helper.EditItemAdapterHelper;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

class EditItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int IMAGE_INFO_SECTION_POSITION = 0;
    private static final int LOCATION_INFO_SECTION_POSITION = 21;
    private static final int DEVICE_INFO_SECTION_POSITION = 47;
    private static final int ADVANCED_INFO_SECTION_POSITION = 59;

    private List<Tag> baseTagsList;
    private OnTagActionListener onTagActionListener;
    private Context context;
    private EditItemAdapterHelper helper;

    EditItemAdapter(EditItemAdapterHelper helper) {
        this.helper = helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType != -1) {
            final View sectionItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_edit_item_view, parent, false);
            return new SectionViewHolder(sectionItemView);
        }

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, parent, false);
        return new TagViewHolder(itemView, onTagActionListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tag tag = getTagsList().get(position);
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
        return getTagsList().size();
    }

    void updateTagList(List<Tag> tags) {
        helper.createGroups(tags);
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            updateBaseTagList(tags);
        } else {
            onTagActionListener.onTagsUpdated();
        }
    }


    void updateBaseTagList(List<Tag> tags) {
        baseTagsList.clear();
        for (Tag tag : tags) {
            baseTagsList.add(new Tag(tag));
        }
    }

    List<Tag> getChangedTags() {
        List<Tag> changedTags = new ArrayList<>();
        for (Tag tag : getTagsList()) {
            for (Tag baseTag : baseTagsList) {
                if (isTagChanged(baseTag, tag)) {
                    changedTags.add(tag);
                    break;
                }
            }
        }
        return changedTags;
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
        List<Tag> tagList = getTagsList();
        if (!tagList.isEmpty()) {
            for (int index = 0; index < tagList.size(); index++) {
                if (tagList.get(index).getKey().equals(tag.getKey())) {
                    tagList.set(index, tag);
                    notifyItemChanged(index);
                    onTagActionListener.onTagsUpdated();
                    break;
                }
            }
        }
    }

    @VisibleForTesting
    List<Tag> getTagsList() {
        return helper.getTagsList();
    }
}
