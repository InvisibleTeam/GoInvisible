package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditViewModelTest {

    private final Tag tag = new Tag("key", "value", TagType.build(InputType.DATE_STRING));

    @Mock
    private EditCompoundRecyclerView editCompoundRecyclerView;

    @Mock
    private TagsManager tagsManager;

    @Mock
    private EditTagCallback listener;

    private EditViewModel editViewModel;

    @Before
    public void setUp() {
        editViewModel = new EditViewModel("title", "image_url", editCompoundRecyclerView, tagsManager, listener);
    }

    @Test
    public void whenTagIsSuccessfullyCleared_ClearingIsPropagate() {
        //Given
        when(tagsManager.clearTag(tag)).thenReturn(true);

        //When
        editViewModel.onClear(tag);

        //Then
        verify(editCompoundRecyclerView).updateTag(tag);
    }

    @Test
    public void whenTagIsUnsuccessfullyCleared_ClearingIsNotPropagate() {
        //Given
        when(tagsManager.clearTag(tag)).thenReturn(false);

        //When
        editViewModel.onClear(tag);

        //Then
        verify(editCompoundRecyclerView, times(0)).updateTag(tag);
    }

    @Test
    public void whenPositionTagIsEdited_PlacePickerViewIsOpened() {
        //Given
        Tag tag = new Tag(
                ExifInterface.TAG_GPS_LATITUDE,
                "value",
                TagType.build(InputType.POSITION_DOUBLE));

        //When
        editViewModel.onEditStarted(tag);

        //Then
        verify(listener).openPlacePickerView(eq(tag));
    }

    @Test
    public void whenTitleIsPassed_TitleAndImageUrlAreProperlySet() {
        //Then
        String title = editViewModel.getTitle().get();
        String imageUrl = editViewModel.getImageUrl().get();

        assertThat(title, is("title"));
        assertThat(imageUrl, is("image_url"));
    }
}
