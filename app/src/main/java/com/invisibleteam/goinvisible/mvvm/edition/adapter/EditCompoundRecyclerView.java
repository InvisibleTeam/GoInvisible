package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.List;

public class EditCompoundRecyclerView extends CompoundRecyclerView<Tag, EditItemAdapter.ViewHolder> {

    private EditItemAdapter itemAdapter;

    public EditCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new EditItemAdapter();
        init(itemAdapter, new LinearLayoutManager(getContext()));
    }

    @Override
    public void updateResults(List<Tag> tags) {
        itemAdapter.updateTagList(tags);
        itemAdapter.notifyDataSetChanged();
    }

    public void prepareTagsList(List<Tag> tags) {
        itemAdapter.prepareTagsList(tags);
        itemAdapter.notifyDataSetChanged();
    }

    public void setOnTagActionListener(OnTagActionListener listener) {
        itemAdapter.setOnTagActionListener(listener);
    }

    public void updateTag(Tag tag) {
        itemAdapter.updateTag(tag);
    }

    @VisibleForTesting
    void setItemAdapter(EditItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public List<Tag> getChangedTags() {
        return itemAdapter.getChangedTags();
    }

    public List<Tag> getAllTags() {
        return itemAdapter.getAllTags();
    }

    public void updateTagListAfterChanges(List<Tag> tags) {
        itemAdapter.updateBaseTagList(tags);
    }
}
