package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.util.TagsManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EditViewModelTest {

    private final Tag tag = new Tag("key", "value", TagType.build(InputType.DATE_STRING), TagGroupType.ADVANCED);

    @Mock
    private EditCompoundRecyclerView editCompoundRecyclerView;

    @Mock
    private TagsManager tagsManager;

    @Mock
    private TagEditionStartCallback listener;

    private EditViewModel editViewModel;

    @Before
    public void setUp() {
        editViewModel = new EditViewModel("title", "image_url", editCompoundRecyclerView, tagsManager, listener);
    }

    @Ignore
    @Test
    public void whenTagIsSuccessfullyCleared_ClearingIsPropagate() {
        //TODO fix this test
        //Given
        //when(tagsManager.clearTag(tag)).thenReturn(true);

        //When
        editViewModel.onClear(tag);

        //Then
        verify(editCompoundRecyclerView).updateTag(tag);
    }

    @Ignore
    @Test
    public void whenAllTagsAreSuccessfullyCleared_TagsAreUpdated() {
        //TODO fix this test
        //Given
        List<Tag> tagList = new ArrayList<>();
        //when(tagsManager.clearTags()).thenReturn(true);

        //When
        editViewModel.onClear(tagList);

        //Then
        //editCompoundRecyclerView.updateResults(...) is also called in EditViewModel constructor.
        verify(editCompoundRecyclerView, times(2)).updateResults(tagList);
    }

    @Ignore
    @Test
    public void whenAllTagsClearFailed_TagsAreNotUpdated() {
        //TODO fix this test
        //Given
        List<Tag> tagList = new ArrayList<>();
        //when(tagsManager.clearTags(anyList())).thenReturn(false);

        //When
        editViewModel.onClear(tagList);

        //Then
        //editCompoundRecyclerView.updateResults(...) is also called in EditViewModel constructor.
        verify(editCompoundRecyclerView, times(1)).updateResults(tagList);
    }

    @Ignore
    @Test
    public void whenTagIsUnsuccessfullyCleared_ClearingIsNotPropagate() {
        //TODO fix this test
        //Given
        //when(tagsManager.clearTag(tag)).thenReturn(false);

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
                TagType.build(InputType.POSITION_DOUBLE),
                TagGroupType.IMAGE_INFO);

        //When
        editViewModel.onEditStarted(tag);

        //Then
        verify(listener).openPlacePickerView(eq(tag));
    }

    @Test
    public void whenUnmodifiableTagIsEdited_UnmodifiableTagMessageIsShown() {
        //Given
        Tag tag = new Tag(
                ExifInterface.TAG_IMAGE_WIDTH,
                "value",
                TagType.build(InputType.UNMODIFIABLE),
                TagGroupType.IMAGE_INFO);

        //When
        editViewModel.onEditStarted(tag);

        //Then
        verify(listener).showUnmodifiableTagMessage();
    }

    @Test
    public void whenTagIsEdited_TagEditionViewIsShown() {
        //When
        editViewModel.onEditStarted(tag);

        //Then
        verify(listener).showTagEditionView(tag);
    }

    @Test
    public void whenIndefiniteTagIsEdited_TagEditionErrorMessageIsShown() {
        //Given
        Tag tag = new Tag(
                ExifInterface.TAG_IMAGE_WIDTH,
                "value",
                TagType.build(InputType.INDEFINITE),
                TagGroupType.IMAGE_INFO);

        //When
        editViewModel.onEditStarted(tag);

        //Then
        verify(listener).showTagEditionErrorMessage();
    }

    @Test
    public void whenTitleIsPassed_TitleAndImageUrlAreProperlySet() {
        //Then
        String title = editViewModel.getTitle().get();
        String imageUrl = editViewModel.getImageUrl().get();

        assertThat(title, is("title"));
        assertThat(imageUrl, is("image_url"));
    }

    @Test
    public void whenTagEditEnded_TagsAreUpdated() {
        //Given
        List<Tag> tagList = new ArrayList<>();

        //When
        editViewModel.onEditEnded(tagList);

        //Then
        //editCompoundRecyclerView.updateResults(...) is also called in EditViewModel constructor.
        verify(editCompoundRecyclerView, times(2)).updateResults(tagList);
    }

    @Test
    public void whenTagsWereUpdated_ViewIsChangedToEditMode() {
        //When
        editViewModel.onTagsUpdated();

        //Then
        //verify(listener).changeViewToEditMode();
    }

    @Test
    public void whenEditionErrorOccurred_TagEditionErrorIsShown() {
        //When
        editViewModel.onEditError();

        //Then
        verify(listener).showTagEditionErrorMessage();
    }
}
