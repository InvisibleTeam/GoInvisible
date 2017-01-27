package com.invisibleteam.goinvisible.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemDecorationAlbumColumnsTest {

    @Mock
    private View view;

    @Mock
    private RecyclerView parent;

    @Mock
    private RecyclerView.State recyclerViewState;

    @Test
    public void whenItemDecorationInitializedAndViewInFirstRowAndColumn_spaceBetweenItemCorrect() {
        //given
        int spaceInPx = 10;
        ItemDecorationAlbumColumns itemDecorationAlbumColumns = new ItemDecorationAlbumColumns(spaceInPx, 2);
        Rect rect = new Rect();
        when(parent.getChildAdapterPosition(any())).thenReturn(0);

        //when
        itemDecorationAlbumColumns.getItemOffsets(rect, view, parent, recyclerViewState);

        //then
        assertEquals(0, rect.top);
        assertEquals(0, rect.bottom);
        assertEquals(0, rect.left);
        assertEquals(spaceInPx/2, rect.right);
    }

    @Test
    public void whenItemDecorationInitializedAndViewInFirstRowAndSecondColumn_spaceBetweenItemCorrect() {
        //given
        int spaceInPx = 10;
        ItemDecorationAlbumColumns itemDecorationAlbumColumns = new ItemDecorationAlbumColumns(spaceInPx, 2);
        Rect rect = new Rect();
        when(parent.getChildAdapterPosition(any())).thenReturn(1);

        //when
        itemDecorationAlbumColumns.getItemOffsets(rect, view, parent, recyclerViewState);

        //then
        assertEquals(0, rect.top);
        assertEquals(0, rect.bottom);
        assertEquals(spaceInPx/2, rect.left);
        assertEquals(0, rect.right);
    }

    @Test
    public void whenItemDecorationInitializedAndViewNotInFirstRowAndInFirstColumn_spaceBetweenItemCorrect() {
        //given
        int spaceInPx = 10;
        ItemDecorationAlbumColumns itemDecorationAlbumColumns = new ItemDecorationAlbumColumns(spaceInPx, 2);
        Rect rect = new Rect();
        when(parent.getChildAdapterPosition(any())).thenReturn(2);

        //when
        itemDecorationAlbumColumns.getItemOffsets(rect, view, parent, recyclerViewState);

        //then
        assertEquals(spaceInPx, rect.top);
        assertEquals(0, rect.bottom);
        assertEquals(0, rect.left);
        assertEquals(spaceInPx/2, rect.right);
    }

    @Test
    public void whenItemDecorationInitializedAndViewNotInFirstRowAndInSecondColumn_spaceBetweenItemCorrect() {
        //given
        int spaceInPx = 10;
        ItemDecorationAlbumColumns itemDecorationAlbumColumns = new ItemDecorationAlbumColumns(spaceInPx, 2);
        Rect rect = new Rect();
        when(parent.getChildAdapterPosition(any())).thenReturn(3);

        //when
        itemDecorationAlbumColumns.getItemOffsets(rect, view, parent, recyclerViewState);

        //then
        assertEquals(spaceInPx, rect.top);
        assertEquals(0, rect.bottom);
        assertEquals(spaceInPx/2, rect.left);
        assertEquals(0, rect.right);
    }
}