package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;
import com.invisibleteam.goinvisible.util.TextViewUtil;

import java.util.ArrayList;
import java.util.List;

class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {

    private List<Tag> tagsList;
    private List<Tag> baseTagsList;
    private EditViewModelCallback editViewModelCallback;

    EditItemAdapter() {
        tagsList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, null, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.editItemViewBinding.clearButton.setOnClickListener(v -> {
            if (editViewModelCallback != null) {
                Tag tag = (Tag) itemView.getTag();
                editViewModelCallback.onClear(tag);
            }
        });
        viewHolder.itemView.setOnClickListener(view -> {
            if (editViewModelCallback != null) {
                Tag tag = (Tag) itemView.getTag();
                editViewModelCallback.onEditStarted(tag);
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

    boolean updateTagList(List<Tag> tags) {
        this.tagsList = tags;
        notifyDataSetChanged();
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            updateBaseTagList(tags);
            return false;
        }
        return true;
    }

    void prepareTagsList(List<Tag> tags) {
        tagsList = tags;
        notifyDataSetChanged();
        baseTagsList = new ArrayList<>();
        updateBaseTagList(tags);
    }

    void updateBaseTagList(List<Tag> tags) {
        baseTagsList.clear();
        for (Tag tag : tags) {
            baseTagsList.add(new Tag(tag));
        }
    }

    void resetBaseTags() {
        updateBaseTagList(tagsList);
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
        if (!tagsList.isEmpty()) {
            for (int index = 0; index < tagsList.size(); index++) {
                if (tagsList.get(index).getKey().equals(tag.getKey())) {
                    tagsList.set(index, tag);
                    notifyItemChanged(index);
                    return true;
                }
            }
        }
        return false;
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
