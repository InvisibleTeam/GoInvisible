package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ObjectType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditViewModelTest {

    private static final Tag TAG = new Tag("key", "value", ObjectType.STRING);

    @Test
    public void whenTagIsSuccessfullyCleared_ClearingIsPropagate() {
        //Given
        EditCompoundRecyclerView editCompoundRecyclerView = mock(EditCompoundRecyclerView.class);
        TagsManager tagsManager = mock(TagsManager.class);
        when(tagsManager.clearTag(TAG)).thenReturn(true);
        EditViewModel editViewModel = new EditViewModel("", "", editCompoundRecyclerView, tagsManager);

        //When
        editViewModel.onClear(TAG);

        //Then
        verify(editCompoundRecyclerView).updateTag(TAG);
    }

    @Test
    public void whenTagIsUnsuccessfullyCleared_ClearingIsNotPropagate() {
        //Given
        EditCompoundRecyclerView editCompoundRecyclerView = mock(EditCompoundRecyclerView.class);
        TagsManager tagsManager = mock(TagsManager.class);
        when(tagsManager.clearTag(TAG)).thenReturn(false);
        EditViewModel editViewModel = new EditViewModel("", "", editCompoundRecyclerView, tagsManager);

        //When
        editViewModel.onClear(TAG);

        //Then
        verify(editCompoundRecyclerView, times(0)).updateTag(TAG);
    }

    @Test
    public void whenTagIsEdited_EditionIsPropagate() {
        //Given
        EditCompoundRecyclerView editCompoundRecyclerView = mock(EditCompoundRecyclerView.class);
        TagsManager tagsManager = mock(TagsManager.class);
        EditViewModel editViewModel = new EditViewModel("", "", editCompoundRecyclerView, tagsManager);

        //When
        editViewModel.onEdit(TAG);

        //Then
        verify(tagsManager).editTag(TAG);
    }

    @Test
    public void whenTitleIsPassed_TitleAndImageUrlAreProperlySet() {
        //Given
        EditCompoundRecyclerView editCompoundRecyclerView = mock(EditCompoundRecyclerView.class);
        TagsManager tagsManager = mock(TagsManager.class);

        //When
        EditViewModel editViewModel = new EditViewModel("title", "image_url", editCompoundRecyclerView, tagsManager);

        //Then
        String title = editViewModel.getTitle().get();
        String imageUrl = editViewModel.getImageUrl().get();

        assertThat(title, is("title"));
        assertThat(imageUrl, is("image_url"));
    }
}