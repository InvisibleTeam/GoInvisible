package com.invisibleteam.goinvisible.mvvm.images;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImagesViewModelTest {

    private ImagesProvider imagesProvider;
    private ImagesCompoundRecyclerView compoundRecyclerView;
    private ImagesViewCallback imagesViewCallback;
    private ArgumentCaptor<ImagesItemAdapter.OnItemClickListener> listenerArgumentCaptor;

    @Before
    public void setUp() {
        compoundRecyclerView = Mockito.mock(ImagesCompoundRecyclerView.class);
        imagesProvider = Mockito.mock(ImagesProvider.class);
        imagesViewCallback = Mockito.mock(ImagesViewCallback.class);
        listenerArgumentCaptor = ArgumentCaptor.forClass(ImagesItemAdapter.OnItemClickListener.class);

    }

    @Test
    public void whenUserClicksOnItem_navigatedToEditScreen() {
        when(imagesProvider.getImagesList()).thenReturn(new ArrayList<>());
        new ImagesViewModel(
                compoundRecyclerView,
                imagesProvider,
                imagesViewCallback);

        verify(compoundRecyclerView).setOnItemClickListener(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onItemClick(new ImageDetails("testPath", "testName"));

        verify(imagesViewCallback).navigateToEdit(any());
    }

    @Test
    public void whenOnUnsupportedItemClickIsCalled_SnackBarWithUnsupportedExtensionIsCalled() {
        when(imagesProvider.getImagesList()).thenReturn(new ArrayList<>());
        new ImagesViewModel(
                compoundRecyclerView,
                imagesProvider,
                imagesViewCallback);

        verify(compoundRecyclerView).setOnItemClickListener(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onUnsupportedItemClick();

        verify(imagesViewCallback).prepareSnackBar(R.string.unsupported_extension);
        verify(imagesViewCallback).showSnackBar();
    }

    @Test
    public void whenOnUnsupportedItemClickIedsCalled_SnackBarWithUnsupportedExtensionIsCalled() {
        when(imagesProvider.getImagesList()).thenReturn(new ArrayList<>());
        ImagesViewModel viewModel = new ImagesViewModel(
                compoundRecyclerView,
                imagesProvider,
                imagesViewCallback);

        viewModel.updateImages();

        verify(imagesProvider, times(2)).getImagesList();
        verify(compoundRecyclerView, times(2)).updateResults(any());
        verify(imagesViewCallback).onStopRefreshingImages();
    }
}