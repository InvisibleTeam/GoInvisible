package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.helper.EditItemAdapterHelper;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;

import java.util.ArrayList;
import java.util.List;

public class EditCompoundRecyclerView extends CompoundRecyclerView<Tag, RecyclerView.ViewHolder> {

    private EditItemAdapter itemAdapter;
    private EditItemAdapterHelper helper = new EditItemAdapterHelper();
    private ArrayList<Tag> baseTagsList = null;

    public EditCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new EditItemAdapter();
        init(itemAdapter, new LinearLayoutManager(getContext()));
    }

    @Override
    public boolean updateResults(List<Tag> tags) {
        return updateTagList(tags);
    }

    public void prepareTagsList(List<Tag> tags) {
        baseTagsList = new ArrayList<>();
        helper.createGroups(tags);
        updateBaseTagList(tags);
        itemAdapter.updateItems(helper.getTagsList());
        itemAdapter.notifyDataSetChanged();
    }

    public void setOnTagActionListener(EditViewModelCallback listener) {
        itemAdapter.setEditViewModelCallback(listener);
    }

    @VisibleForTesting
    void setItemAdapter(EditItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public List<Tag> getChangedTags() {
        List<Tag> changedTags = new ArrayList<>();
        for (Tag tag : helper.getTagsList()) {
            for (Tag baseTag : baseTagsList) {
                if (isTagChanged(baseTag, tag)) {
                    changedTags.add(tag);
                    break;
                }
            }
        }
        return changedTags;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public List<Tag> getAllTags() {
        return helper.getTagsList();
    }

    public boolean updateTag(Tag tag) {
        List<Tag> tagList = helper.getTagsList();
        if (tagList.isEmpty()) {
            return false;
        }

        for (int index = 0; index < tagList.size(); index++) {
            if (tagList.get(index).getKey().equals(tag.getKey())) {
                tagList.set(index, tag);
                itemAdapter.notifyItemChanged(index);
                return true;
            }
        }

        return false;
    }

    boolean updateTagList(List<Tag> tags) {
        boolean updated = true;
        if (baseTagsList == null) {
            baseTagsList = new ArrayList<>();
            helper.createGroups(tags);
            updateBaseTagList(tags);
            updated = false;
        } else {
            helper.createGroups(tags);
        }
        itemAdapter.updateItems(helper.getTagsList());
        itemAdapter.notifyDataSetChanged();
        return updated;
    }

    void updateBaseTagList(List<Tag> tags) {
        baseTagsList.clear();
        for (Tag tag : tags) {
            baseTagsList.add(new Tag(tag));
        }
    }

    public void updateTagListAfterChanges() {
        updateBaseTagList(helper.getTagsList());
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
}
