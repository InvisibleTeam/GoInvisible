package com.invisibleteam.goinvisible.mvvm.images;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImagesViewModelTest {

    @Test
    public void whenUserClicksOnItem_navigatedToEditScreen() {
        ImagesCompoundRecyclerView mockedCompoundRecyclerView = Mockito.mock(ImagesCompoundRecyclerView.class);
        ImagesProvider mockedImagesProvider = Mockito.mock(ImagesProvider.class);
        ImagesView mockedImagesView = Mockito.mock(ImagesView.class);
        ArgumentCaptor<ImagesItemAdapter.OnItemClickListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(ImagesItemAdapter.OnItemClickListener.class);
        when(mockedImagesProvider.getImagesList()).thenReturn(new ArrayList<>());
        new ImagesViewModel(mockedCompoundRecyclerView,
                mockedImagesProvider,
                mockedImagesView);

        verify(mockedCompoundRecyclerView).setOnItemClickListener(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onItemClick(new ImageDetails("testPath", "testName"));

        verify(mockedImagesView).navigateToEdit(any());
    }
}