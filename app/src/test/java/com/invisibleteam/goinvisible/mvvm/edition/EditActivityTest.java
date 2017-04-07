package com.invisibleteam.goinvisible.mvvm.edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.IntentMatcher.containsSameData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
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
    private ImageDetails imageDetails;

    @Before
    public void init() {
        context = RuntimeEnvironment.application;
        imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);
        activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(editActivityIntent)
                .create()
                .get();
    }

    @Test
    public void whenEditActivityBuildIsInitiated_EditActivityIntentIsCreated() {
        //Given
        Bundle bundle = new Bundle();
        bundle.putParcelable("extra_image_details", imageDetails);
        Intent expectedIntent = new Intent(context, EditActivity.class);
        expectedIntent.putExtras(bundle);

        //When
        Intent intent = EditActivity.buildIntent(context, imageDetails);

        //Then
        assertThat(intent, containsSameData(expectedIntent));
    }

    @Test
    public void whenProperIntentIsPassed_ExtractionFinishWithSuccess() {
        //When
        boolean isExtractionSucceed = activity.extractBundle();

        //Then
        assertTrue(isExtractionSucceed);
    }

    @Test
    public void whenUnknownOptionItemSelected_DefaultMethodIsCalled() {
        //given
        activity = spy(activity);
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
        Intent intent = new Intent(context, Activity.class);
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
    public void whenAllNecessaryPermissionsAreGranted_PrepareViewIsCalled() {
        //Given
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).get();
        EditActivity spyActivity = spy(activity);

        //When
        spyActivity.onCreate(null);

        //Then
        verify(spyActivity).prepareView();
    }

    private Tag createTag(String key, String value) {
        return new Tag(key, value, TagType.build(InputType.DATETIME_STRING), TagGroupType.ADVANCED);
    }
}
