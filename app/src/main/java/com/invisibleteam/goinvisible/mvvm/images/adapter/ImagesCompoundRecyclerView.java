package com.invisibleteam.goinvisible.mvvm.images.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;

import java.util.List;

public class ImagesCompoundRecyclerView extends FrameLayout {

    private RecyclerView recyclerView;
    private ImagesItemAdapter imageItemAdapter;

    public ImagesCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        View view = LayoutInflater
                .from(getContext())
                .inflate(R.layout.images_listview, this, true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        imageItemAdapter = new ImagesItemAdapter();
        recyclerView.setAdapter(imageItemAdapter);
    }

    public void updateResults(List<ImageDetails> imageList) {
        imageItemAdapter.updateImageList(imageList);
        imageItemAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(ImagesItemAdapter.OnItemClickListener listener) {
        imageItemAdapter.setOnItemClickListener(listener);
    }
}
