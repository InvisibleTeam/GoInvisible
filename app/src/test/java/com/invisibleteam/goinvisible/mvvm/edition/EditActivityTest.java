package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.media.ExifInterface;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditActivityTest {

    private Context context;
    private EditActivity activity;

    @Before
    public void init() {
        context = RuntimeEnvironment.application;
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);
        activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(editActivityIntent)
                .create()
                .get();
        activity = spy(activity);
    }

    @Test
    public void whenProperIntentIsPassed_ExtractionFinishWithSuccess() {
        //When
        boolean isExtractionSucceed = activity.extractBundle();

        //Then
        assertTrue(isExtractionSucceed);
    }

    @Test
    public void whenOptionItemIsSelectedAndTagsAreChanged_TagsAreSavedAndBackToImageActivityIsCalled() {
        //Given
        List<Tag> tagsList = Arrays.asList(
                createTag("key1", "value1"),
                createTag("key2", "value2"));

        EditCompoundRecyclerView recyclerView = mock(EditCompoundRecyclerView.class);
        when(recyclerView.getChangedTags()).thenReturn(tagsList);

        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);

        activity.setEditCompoundRecyclerView(recyclerView);
        Mockito.doNothing().when(activity).showApproveChangeTagsDialog();

        //when
        activity.onOptionsItemSelected(menuItem);

        //then
        verify(recyclerView).getChangedTags();
        verify(activity).showApproveChangeTagsDialog();
        verify(activity).onBackPressed();
    }

    @Test
    public void whenOptionItemIsSelectedAndTagsAreNotChanged_OnlyBackToImageActivityIsCalled() {
        //Given
        EditCompoundRecyclerView recyclerView = mock(EditCompoundRecyclerView.class);
        when(recyclerView.getChangedTags()).thenReturn(new ArrayList<>());

        activity.setEditCompoundRecyclerView(recyclerView);

        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);

        //when
        activity.onOptionsItemSelected(menuItem);

        //then
        verify(recyclerView).getChangedTags();
        verify(activity, times(0)).showApproveChangeTagsDialog();
        verify(activity).onBackPressed();
    }

    @Test
    public void whenUnknownOptionItemSelected_defaultMethodIsCalled() {
        //given
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(-1);

        //when
        activity.onOptionsItemSelected(menuItem);

        //then
        verify(activity, times(0)).onBackPressed();
    }

    @Test
    public void whenNullIntentIsPassed_ExtractionFinishWithFailure() {
        //Given
        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(null)
                .get();

        //When
        boolean isExtractionFailure = !activity.extractBundle();

        //Then
        assertTrue(isExtractionFailure);
    }

    @Test
    public void whenWrongIntentIsPassed_ExtractionFinishWithFailure() {
        //Given
        Bundle bundle = new Bundle();
        bundle.putParcelable("filePath", mock(Parcelable.class));
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtras(bundle);

        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(intent)
                .get();

        //When
        boolean isExtractionFailure = !activity.extractBundle();

        //Then
        assertTrue(isExtractionFailure);
    }

    @Test
    public void whenProperIntentIsPassed_ViewModelIsInitiated() throws IOException {
        Intent intent = EditActivity.buildIntent(context, new ImageDetails("Path", "Name"));
        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(intent)
                .get();

        EditActivity mockEditActivity = spy(activity);
        ExifInterface exifInterface = mock(ExifInterface.class);
        doReturn(exifInterface).when(mockEditActivity).getExifInterface();

        //When
        mockEditActivity.onCreate(null);
        boolean isViewModelInitiated = mockEditActivity.getEditViewModel() != null;

        //Then
        assertTrue(isViewModelInitiated);
    }

    @Test
    public void whenNullIntentIsPassed_ViewModelIsNotInitiated() {
        //Given
        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(null)
                .get();

        //When
        activity.onCreate(null);
        boolean isViewModelNotInitiated = activity.getEditViewModel() == null;

        //Then
        assertTrue(isViewModelNotInitiated);
    }

    @Test
    public void whenAllNecessaryPermissionsAreGranted_prepareViewIsCalled() {
        //Given
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).get();
        EditActivity spyActivity = spy(activity);

        //When
        spyActivity.onCreate(null);

        //Then
        verify(spyActivity).prepareView();
    }

    private Tag createTag(String key, String value) {
        return new Tag(key, value, TagType.build(InputType.DATETIME_STRING));
    }
}
