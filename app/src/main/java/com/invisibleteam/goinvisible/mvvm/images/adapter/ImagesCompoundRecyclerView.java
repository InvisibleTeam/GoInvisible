package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.CompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ItemDecorationAlbumColumns;

import java.util.List;

public class ImagesCompoundRecyclerView extends CompoundRecyclerView<ImageDetails, ImagesItemAdapter.ViewHolder> {

    private static final int GRID_COLUMN_COUNT = 2;

    private final ImagesItemAdapter itemAdapter;

    public ImagesCompoundRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        itemAdapter = new ImagesItemAdapter();
        init(itemAdapter, new GridLayoutManager(getContext(), GRID_COLUMN_COUNT));
    }

    @Override
    protected void initRecyclerViewItemDecorators(RecyclerView recyclerView) {
        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.images_grid_margin);
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(marginInPixels, GRID_COLUMN_COUNT));
    }

    @Override
    public boolean updateResults(List<ImageDetails> imageList) {
        itemAdapter.updateImageList(imageList);
        return true;
    }

    public void setOnItemClickListener(ImagesItemAdapter.OnItemClickListener listener) {
        itemAdapter.setOnItemClickListener(listener);
    }
}
