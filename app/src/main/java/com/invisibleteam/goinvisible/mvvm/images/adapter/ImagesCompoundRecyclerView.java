package com.invisibleteam.goinvisible.mvvm.images.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;

import java.util.List;

public class ImagesCompoundRecyclerView extends CompoundRecyclerView<ImageDetails, ImagesItemAdapter.ViewHolder> {

    private final ImagesItemAdapter itemAdapter;

    public ImagesCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new ImagesItemAdapter();
        init(itemAdapter, new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void updateResults(List<ImageDetails> imageList) {
        itemAdapter.updateImageList(imageList);
        itemAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(ImagesItemAdapter.OnItemClickListener listener) {
        itemAdapter.setOnItemClickListener(listener);
    }
}
