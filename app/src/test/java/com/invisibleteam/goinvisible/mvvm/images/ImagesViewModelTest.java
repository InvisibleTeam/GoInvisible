package com.invisibleteam.goinvisible.mvvm.images;

import android.widget.ImageView;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesItemAdapter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImagesViewModelTest {

    @Test
    public void whenUserClicksOnItem_navigatedToEditScreen() {
        //Given
        ImagesCompoundRecyclerView mockedCompoundRecyclerView = Mockito.mock(ImagesCompoundRecyclerView.class);
        ImagesProvider mockedImagesProvider = Mockito.mock(ImagesProvider.class);
        ImagesView mockedImagesView = Mockito.mock(ImagesView.class);
        ArgumentCaptor<ImagesItemAdapter.OnItemClickListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(ImagesItemAdapter.OnItemClickListener.class);
        when(mockedImagesProvider.getImagesList()).thenReturn(new ArrayList<>());
        new ImagesViewModel(mockedCompoundRecyclerView,
                mockedImagesProvider,
                mockedImagesView);
        ImageView imageView = mock(ImageView.class);

        //When
        verify(mockedCompoundRecyclerView).setOnItemClickListener(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onItemClick(imageView, new ImageDetails("testPath", "testName"));

        //Then
        verify(mockedImagesView).navigateToEdit(any(), any());
    }
}