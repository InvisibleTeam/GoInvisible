package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.view.ViewGroup;

import com.google.common.collect.ImmutableList;
import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.ImagesActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter.JPEG_IMAGE;
import static com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter.UNSUPPORTED_EXTENSION_IMAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImagesItemAdapterTest {

    private static final List<ImageDetails> IMAGES_DETAILS_LIST = ImmutableList.of(new ImageDetails("Path", "Name"));

    private ImagesActivity activity;
    private ImagesItemAdapter imagesItemAdapter;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
        imagesItemAdapter = new ImagesItemAdapter();
    }

    @Test
    public void whenViewHolderIsCreatedWithNotEmptyList_thenAdapterHasResults() {
        //given
        ViewGroup parent = mock(ViewGroup.class);
        when(parent.getContext()).thenReturn(activity);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter.onCreateViewHolder(parent, 0);

        //when
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);

        //then
        assertEquals(1, imagesItemAdapter.getItemCount());
    }

    @Test
    public void whenAdapterItemIsClicked_thenOnItemClickIsCalled() {
        //given
        ViewGroup parent = mock(ViewGroup.class);
        when(parent.getContext()).thenReturn(activity);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter.onCreateViewHolder(parent, JPEG_IMAGE);

        ImageDetails imageDetails = IMAGES_DETAILS_LIST.get(0);
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        ImagesItemAdapter.OnItemClickListener onItemClickListener = mock(ImagesItemAdapter.OnItemClickListener.class);

        //when
        imagesItemAdapter.setOnItemClickListener(onItemClickListener);
        viewHolder.itemView.performClick();

        //then
        verify(onItemClickListener).onItemClick(imageDetails);
    }

    @Test
    public void whenAdapterUnsupportedtemIsClicked_thenOnUnsupportedItemClickIsCalled() {
        //given
        ViewGroup parent = mock(ViewGroup.class);
        when(parent.getContext()).thenReturn(activity);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter
                .onCreateViewHolder(parent, UNSUPPORTED_EXTENSION_IMAGE);

        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        ImagesItemAdapter.OnItemClickListener onItemClickListener = mock(ImagesItemAdapter.OnItemClickListener.class);

        //when
        imagesItemAdapter.setOnItemClickListener(onItemClickListener);
        viewHolder.itemView.performClick();

        //then
        verify(onItemClickListener).onUnsupportedItemClick();
    }

}