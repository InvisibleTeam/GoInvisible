package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static com.invisibleteam.goinvisible.mvvm.images.ImagesActivity.TAGS_CHANGED_EXTRA;
import static com.invisibleteam.goinvisible.util.IntentMatcher.containsSameData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
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
        expectedIntent.putExtra(ImagesActivity.TAGS_CHANGED_EXTRA, true);
        expectedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //When
        Intent intent = ImagesActivity.buildIntent(context);

        //Then
        assertThat(intent, containsSameData(expectedIntent));
    }

    @Test
    public void whenActivityIsStarted_IntentExtractionAndSnackbarAreCalled() {
        //Given
        Intent intent = new Intent();
        intent.putExtra(TAGS_CHANGED_EXTRA, "");

        //When
        activity = spy(Robolectric.buildActivity(ImagesActivity.class)
                .withIntent(intent)
                .get());

        ImagesCallback callback = mock(ImagesCallback.class);
        when(activity.buildImagesCallback(any())).thenReturn(callback);

        activity.onCreate(null);

        verify(activity).extractBundle();
        verify(callback).prepareSnackBar(R.string.tags_changed_message);
        verify(callback).showSnackBar();
    }
}
