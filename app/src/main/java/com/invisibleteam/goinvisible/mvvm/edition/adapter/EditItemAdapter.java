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
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

class EditItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    static final int IMAGE_INFO_SECTION_POSITION = 0;
    static final int LOCATION_INFO_SECTION_POSITION = 21;
    static final int DEVICE_INFO_SECTION_POSITION = 47;
    static final int ADVANCED_INFO_SECTION_POSITION = 59;
    static final int DEFAULT_VIEW_TYPE = -1;

    private List<Tag> baseTagsList;
    private EditViewModelCallback editViewModelCallback;
    private Context context;
    private final EditItemAdapterHelper helper;

    EditItemAdapter(EditItemAdapterHelper helper) {
        this.helper = helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType != DEFAULT_VIEW_TYPE) {
            final View sectionItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_edit_item_view, parent, false);
            return new SectionViewHolder(sectionItemView);
        }

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, parent, false);
        return new TagViewHolder(itemView, editViewModelCallback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tag tag = getTagsList().get(position);

        if (holder instanceof SectionViewHolder) {
            final String sectionName = tag.getTagGroupTypeName(context);
            ((SectionViewHolder) holder).setSectionName(sectionName);
        } else {
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            tagViewHolder.setTag(tag);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case IMAGE_INFO_SECTION_POSITION:
                return IMAGE_INFO.ordinal();
            case LOCATION_INFO_SECTION_POSITION:
                return LOCATION_INFO.ordinal();
            case DEVICE_INFO_SECTION_POSITION:
                return DEVICE_INFO.ordinal();
            case ADVANCED_INFO_SECTION_POSITION:
                return ADVANCED.ordinal();
            default:
                return DEFAULT_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return getTagsList().size();
    }

    boolean updateTagList(List<Tag> tags) {
        notifyDataSetChanged();
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            helper.createGroups(tags);
            updateBaseTagList(tags);
            return false;
        }
        return true;
    }

    void prepareTagsList(List<Tag> tags) {
        notifyDataSetChanged();
        baseTagsList = new ArrayList<>();
        helper.createGroups(tags);
        updateBaseTagList(tags);
    }


    void updateBaseTagList(List<Tag> tags) {
        baseTagsList.clear();
        for (Tag tag : tags) {
            baseTagsList.add(new Tag(tag));
        }
    }

    void resetBaseTags() {
        updateBaseTagList(getTagsList());
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

    void setEditViewModelCallback(EditViewModelCallback callback) {
        this.editViewModelCallback = callback;
    }

    boolean updateTag(Tag tag) {
        List<Tag> tagList = getTagsList();
        if (tagList.isEmpty()) {
            return false;
        }

        for (int index = 0; index < tagList.size(); index++) {
            if (tagList.get(index).getKey().equals(tag.getKey())) {
                tagList.set(index, tag);
                notifyItemChanged(index);
                return true;
            }
        }

        return false;
    }

    @VisibleForTesting
    List<Tag> getTagsList() {
        return helper.getTagsList();
    }
}
