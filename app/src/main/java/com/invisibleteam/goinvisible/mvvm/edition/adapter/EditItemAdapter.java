package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.TextViewUtil;

import java.util.ArrayList;
import java.util.List;

class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {

    private List<Tag> tagsList;
    private List<Tag> baseTagsList;
    private OnTagActionListener onTagActionListener;

    EditItemAdapter() {
        tagsList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, null, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.editItemViewBinding.clearButton.setOnClickListener(v -> {
            if (onTagActionListener != null) {
                Tag tag = (Tag) itemView.getTag();
                onTagActionListener.onClear(tag);
            }
        });
        viewHolder.itemView.setOnClickListener(view -> {
            if (onTagActionListener != null) {
                Tag tag = (Tag) itemView.getTag();
                onTagActionListener.onEditStarted(tag);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tag tag = tagsList.get(position);
        holder.itemView.setTag(tag);
        holder.editItemViewModel.setModel(tag);
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    void updateImageList(List<Tag> imageList) {
        this.tagsList = imageList;
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            for (Tag tag : imageList) {
                baseTagsList.add(new Tag(tag));
            }
        } else {
            onTagActionListener.onTagsUpdated();
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

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        EditItemViewModel editItemViewModel;

        EditItemViewBinding editItemViewBinding;

        ViewHolder(View itemView) {
            super(itemView);

            editItemViewModel = new EditItemViewModel();
            editItemViewBinding = EditItemViewBinding.bind(itemView);
            editItemViewBinding.setViewModel(editItemViewModel);
            TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
        }
    }
}
