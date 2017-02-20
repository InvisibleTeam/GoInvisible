package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImagesActivityTest {

    private ImagesActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
    }

    @Test
    public void whenNavigateToEditIsCalled_thenEditActivityWillBeStarted() {
        //given
        ImageDetails imageDetails = new ImageDetails("ImagePath", "ImageName");
        ShadowActivity shadowActivity = shadowOf(activity);

        //when
        activity.getImagesCallback().navigateToEdit(imageDetails);

        //then
        Intent editActivityIntent = shadowActivity.peekNextStartedActivity();
        ImageDetails editActivityImageDetails = editActivityIntent.getParcelableExtra("extra_image_details");
        assertNotNull(editActivityImageDetails);
        assertEquals("ImagePath", editActivityImageDetails.getPath());
        assertEquals("ImageName", editActivityImageDetails.getName());
        assertEquals(new ComponentName(activity, EditActivity.class), editActivityIntent.getComponent());
    }

    @Test
    public void whenSettingsOptionIsClicked_SettingsActivityIsStarted() {
        //Given
        MenuItem menuItem = mock(MenuItem.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        when(menuItem.getItemId()).thenReturn(R.id.settings);

        //When
        activity.onOptionsItemSelected(menuItem);

        //Then
        Intent settingActivity = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, SettingsActivity.class), settingActivity.getComponent());
    }

    @Test
    public void whenSupportedImageIsClicked_EditActivityIsStarted() {
        //When
        activity.getImagesCallback().navigateToEdit(new ImageDetails("path", "name"));

        //Then
        Intent startedIntent = shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(EditActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void whenImagesActivityBuildIsInitiated_ImagesActivityIntentIsCreated() {
        //Given
        Context context = RuntimeEnvironment.application;
        Intent expectedIntent = new Intent(context, ImagesActivity.class);

        //When
        Intent intent = ImagesActivity.buildIntent(context);

        //Then
        assertThat(intent.getComponent(), is(expectedIntent.getComponent()));
    }
}
