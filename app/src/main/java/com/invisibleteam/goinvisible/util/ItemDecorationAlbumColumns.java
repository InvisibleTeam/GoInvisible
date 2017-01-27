package com.invisibleteam.goinvisible.util;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecorationAlbumColumns extends RecyclerView.ItemDecoration {

    private int gridSpacingPx;
    private int gridSize;

    public ItemDecorationAlbumColumns(int gridSpacingPx, int gridSize) {
        this.gridSpacingPx = gridSpacingPx;
        this.gridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int itemColumn = itemPosition % gridSize;
        outRect.left = itemColumn * gridSpacingPx / gridSize;
        outRect.right = gridSpacingPx - (itemColumn + 1) * gridSpacingPx / gridSize;
        if (itemPosition >= gridSize) {
            outRect.top = gridSpacingPx;
        }
    }
}
