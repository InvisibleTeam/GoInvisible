package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

class EditItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int IMAGE_INFO_SECTION_POSITION = 0;
    static final int LOCATION_INFO_SECTION_POSITION = 21;
    static final int DEVICE_INFO_SECTION_POSITION = 47;
    static final int ADVANCED_INFO_SECTION_POSITION = 59;
    static final int DEFAULT_VIEW_TYPE = -1;

    private List<Tag> tagList = new ArrayList<>();
    private EditViewModelCallback editViewModelCallback;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != DEFAULT_VIEW_TYPE) {
            final View sectionItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_edit_item_view, parent, false);
            return new SectionViewHolder(sectionItemView);
        }

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, parent, false);
        return new TagViewHolder(itemView, editViewModelCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        if (holder instanceof SectionViewHolder) {
            String sectionName = tag.getTagGroupTypeName(holder.itemView.getContext());
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
        return tagList.size();
    }

    void updateItems(List<Tag> items) {
        this.tagList = items;
    }

    void setEditViewModelCallback(EditViewModelCallback callback) {
        this.editViewModelCallback = callback;
    }
}
