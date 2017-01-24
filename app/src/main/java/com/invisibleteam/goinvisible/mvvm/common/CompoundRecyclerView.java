package com.invisibleteam.goinvisible.mvvm.common;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.invisibleteam.goinvisible.R;

import java.util.List;

public abstract class CompoundRecyclerView<T, K extends RecyclerView.ViewHolder> extends FrameLayout {

    public CompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init(RecyclerView.Adapter<K> adapter, RecyclerView.LayoutManager manager) {
        View view = LayoutInflater
                .from(getContext())
                .inflate(R.layout.listview, this, true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public abstract void updateResults(List<T> imageList);
}
