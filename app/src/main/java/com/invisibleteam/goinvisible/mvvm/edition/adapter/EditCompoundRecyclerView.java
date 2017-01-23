package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.Tag;

import java.util.List;

public class EditCompoundRecyclerView extends FrameLayout {

    private RecyclerView recyclerView;
    private EditItemAdapter editItemAdapter;

    public EditCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        View view = LayoutInflater
                .from(getContext())
                .inflate(R.layout.listview, this, true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        editItemAdapter = new EditItemAdapter();
        recyclerView.setAdapter(editItemAdapter);
    }

    public void updateResults(List<Tag> imageList) {
        editItemAdapter.updateImageList(imageList);
        editItemAdapter.notifyDataSetChanged();
    }
}
