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
    public boolean updateResults(List<Tag> tags) {
        return itemAdapter.updateTagList(tags);
    }

    public void prepareTagsList(List<Tag> tags) {
        itemAdapter.prepareTagsList(tags);
    }

    public void setOnTagActionListener(OnTagActionListener listener) {
        itemAdapter.setOnTagActionListener(listener);
    }

    public boolean updateTag(Tag tag) {
        return itemAdapter.updateTag(tag);
    }

    @VisibleForTesting
    void setItemAdapter(EditItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public List<Tag> getChangedTags() {
        return itemAdapter.getChangedTags();
    }

    public void updateTagListAfterChanges() {
        itemAdapter.resetBaseTags();
    }
}
