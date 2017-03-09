package com.invisibleteam.goinvisible.mvvm.images;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImagesViewModelTest {

    private MockImagesViewModel mockImagesViewModel;
    private ImagesProvider imagesProvider;
    private ImagesCompoundRecyclerView compoundRecyclerView;
    private ImagesViewCallback imagesViewCallback;
    private ArgumentCaptor<ImagesItemAdapter.OnItemClickListener> listenerArgumentCaptor;

    @Before
    public void setUp() {
        compoundRecyclerView = mock(ImagesCompoundRecyclerView.class);
        imagesProvider = mock(ImagesProvider.class);
        imagesViewCallback = mock(ImagesViewCallback.class);
        listenerArgumentCaptor = ArgumentCaptor.forClass(ImagesItemAdapter.OnItemClickListener.class);

        mockImagesViewModel = new MockImagesViewModel(compoundRecyclerView, imagesProvider, imagesViewCallback);
        mockImagesViewModel = spy(mockImagesViewModel);
    }

    @Test
    public void whenOnUnsupportedItemClickIsCalled_SnackBarWithUnsupportedExtensionIsCalled() {
        //Given
        when(imagesProvider.getImagesList()).thenReturn(new ArrayList<>());

        //When
        verify(compoundRecyclerView).setOnItemClickListener(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onUnsupportedItemClick();

        //Then
        verify(imagesViewCallback).prepareSnackBar(R.string.unsupported_extension);
        verify(imagesViewCallback).showSnackBar();
    }

    @Test
    public void whenUpdateImagesMethodIsCalled_ImagesAreUpdated() {
        //Given
        when(imagesProvider.getImagesList()).thenReturn(new ArrayList<>());

        //When
        mockImagesViewModel.updateImages();

        //Then
        verify(imagesProvider, times(2)).getImagesList();
        verify(compoundRecyclerView, times(2)).updateResults(any());
        verify(imagesViewCallback).onStopRefreshingImages();
    }
}