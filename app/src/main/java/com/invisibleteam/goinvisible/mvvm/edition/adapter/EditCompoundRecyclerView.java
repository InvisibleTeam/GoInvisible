package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;

import java.util.List;

public class EditCompoundRecyclerView extends CompoundRecyclerView<Tag, EditItemAdapter.ViewHolder> {

    private final EditItemAdapter itemAdapter;

    public EditCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new EditItemAdapter();
        init(itemAdapter, new GridLayoutManager(getContext(), 1));
    }

    public void updateResults(List<Tag> imageList) {
        itemAdapter.updateImageList(imageList);
        itemAdapter.notifyDataSetChanged();
    }
}
