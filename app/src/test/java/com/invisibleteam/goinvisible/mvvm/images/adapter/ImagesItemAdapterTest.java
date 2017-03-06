package com.invisibleteam.goinvisible.mvvm.images.adapter;

import android.util.Log;
import android.widget.LinearLayout;

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

import java.util.Collections;
import java.util.List;

import static com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter.JPEG_IMAGE;
import static com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter.UNSUPPORTED_EXTENSION_IMAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImagesItemAdapterTest {

    private static final ImageDetails IMAGES_DETAILS = new ImageDetails("Path", "Name");
    private static final List<ImageDetails> IMAGES_DETAILS_LIST = ImmutableList.of(IMAGES_DETAILS);
    private static final String TAG = ImagesItemAdapterTest.class.getSimpleName();

    private ImagesActivity activity;
    private ImagesItemAdapter imagesItemAdapter;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
        imagesItemAdapter = new ImagesItemAdapter();
    }

    @Test
    public void whenViewHolderIsCreatedWithNotEmptyList_AdapterHasResults() {
        //Given
        LinearLayout parent = new LinearLayout(activity);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter.onCreateViewHolder(parent, JPEG_IMAGE);

        //When
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);

        //Then
        assertEquals(1, imagesItemAdapter.getItemCount());
    }

    @Test()
    public void whenViewHolderIsJpegImageViewHolder_ModelIsSet() {
        //Given
        ImagesItemAdapter.JpegImageViewHolder viewHolder = mock(ImagesItemAdapter.JpegImageViewHolder.class);
        ImageItemViewModel viewModel = mock(ImageItemViewModel.class);
        when(viewHolder.getViewModel()).thenReturn(viewModel);
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);

        //When
        try {
            imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        } catch (NullPointerException e) {
            Log.d(TAG, "Concious NullpointerException");
            //This exception is thrown because that we cannot mock itemView in holder
        }

        //Then
        verify(viewModel).setModel(IMAGES_DETAILS);
    }

    @Test()
    public void whenViewHolderIsUnsupportedImageViewHolder_ModelIsSet() {
        //Given
        ImagesItemAdapter.UnsupportedImageViewHolder viewHolder =
                mock(ImagesItemAdapter.UnsupportedImageViewHolder.class);
        ImageItemViewModel viewModel = mock(ImageItemViewModel.class);
        when(viewHolder.getViewModel()).thenReturn(viewModel);
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);

        //When
        try {
            imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        } catch (NullPointerException e) {
            Log.d(TAG, "Concious NullpointerException");
            //This exception is thrown because that we cannot mock itemView in holder
        }

        //Then
        verify(viewModel).setModel(IMAGES_DETAILS);
    }

    @Test
    public void whenAdapterItemIsClicked_OnItemClickIsCalled() {
        //Given
        LinearLayout parent = new LinearLayout(activity);
        parent = spy(parent);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter.onCreateViewHolder(parent, JPEG_IMAGE);

        ImageDetails imageDetails = IMAGES_DETAILS_LIST.get(0);
        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        ImagesItemAdapter.OnItemClickListener onItemClickListener = mock(ImagesItemAdapter.OnItemClickListener.class);

        //When
        imagesItemAdapter.setOnItemClickListener(onItemClickListener);
        viewHolder.itemView.performClick();

        //Then
        verify(onItemClickListener).onItemClick(imageDetails);
    }

    @Test
    public void whenAdapterUnsupportedItemIsClicked_OnUnsupportedItemClickIsCalled() {
        //Given
        LinearLayout parent = new LinearLayout(activity);
        parent = spy(parent);
        ImagesItemAdapter.ViewHolder viewHolder = imagesItemAdapter
                .onCreateViewHolder(parent, UNSUPPORTED_EXTENSION_IMAGE);

        imagesItemAdapter.updateImageList(IMAGES_DETAILS_LIST);
        imagesItemAdapter.onBindViewHolder(viewHolder, 0);
        ImagesItemAdapter.OnItemClickListener onItemClickListener = mock(ImagesItemAdapter.OnItemClickListener.class);

        //When
        imagesItemAdapter.setOnItemClickListener(onItemClickListener);
        viewHolder.itemView.performClick();

        //Then
        verify(onItemClickListener).onUnsupportedItemClick();
    }

    @Test
    public void whenImageIsJpeg_JpegViewTypeIsReturned() {
        //Given
        ImageDetails imageDetails = new ImageDetails("test.jpg", "name");
        imagesItemAdapter.updateImageList(Collections.singletonList(imageDetails));

        //When
        int viewType = imagesItemAdapter.getItemViewType(0);

        //Then
        assertEquals(viewType, JPEG_IMAGE);
    }

    @Test
    public void whenImageIsPng_UnsupportedViewTypeIsReturned() {
        //Given
        ImageDetails imageDetails = new ImageDetails("test.png", "name");
        imagesItemAdapter.updateImageList(Collections.singletonList(imageDetails));

        //When
        int viewType = imagesItemAdapter.getItemViewType(0);

        //Then
        assertEquals(viewType, UNSUPPORTED_EXTENSION_IMAGE);
    }
}