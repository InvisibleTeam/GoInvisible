package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.helper.EditItemAdapterHelper;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import java.util.List;

public class EditCompoundRecyclerView extends CompoundRecyclerView<Tag, ViewHolder> {

    private EditItemAdapter itemAdapter;

    public EditCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new EditItemAdapter(new EditItemAdapterHelper());
        init(itemAdapter, new LinearLayoutManager(getContext()));
    }

    @Override
    public void updateResults(List<Tag> imageList) {
        itemAdapter.updateTagList(imageList);
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
        return itemAdapter.getTagsList();
    }

    public void updateTagListAfterChanges(List<Tag> tags) {
        itemAdapter.updateBaseTagList(tags);
    }
}
