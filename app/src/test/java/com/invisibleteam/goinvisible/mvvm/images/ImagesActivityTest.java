package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ComponentName;
import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
public class ImagesActivityTest {

    private ImagesActivity activity;

    @Before
    public void setUp() {
        activity = spy(Robolectric.buildActivity(ImagesActivity.class).create().get());
    }

    @Test
    public void whenNavigateToEditIsCalled_thenEditActivityWillBeStarted() {
        //given
        ImageDetails imageDetails = new ImageDetails("ImagePath", "ImageName");
        ShadowActivity shadowActivity = shadowOf(activity);

        //when
        activity.navigateToEdit(imageDetails);

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
        activity.navigateToEdit(new ImageDetails("path", "name"));

        //Then
        Intent startedIntent = shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(EditActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void whenUnSupportedImageIsClicked_SnackbarWithInformationIsAppeared() {
        //Given
        Snackbar snackbar = mock(Snackbar.class);
        activity.setSnackbar(snackbar);

        //When
        activity.showUnsupportedImageInfo();

        //Then
        verify(snackbar).show();
    }
}