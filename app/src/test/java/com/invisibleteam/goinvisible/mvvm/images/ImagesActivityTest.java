package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ComponentName;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
public class ImagesActivityTest {

    private ImagesActivity spyActivity;

    @Before
    public void setUp() {
        spyActivity = spy(Robolectric.buildActivity(ImagesActivity.class).create().get());
    }

    @Test
    public void whenNavigateToEditIsCalled_thenEditActivityWillBeStarted() {
        //given
        ImageDetails imageDetails = new ImageDetails("ImagePath", "ImageName");
        ShadowActivity shadowActivity = Shadows.shadowOf(spyActivity);

        //when
        spyActivity.navigateToEdit(imageDetails);

        //then
        Intent editActivityIntent = shadowActivity.peekNextStartedActivity();
        ImageDetails editActivityImageDetails = editActivityIntent.getParcelableExtra("extra_image_details");
        assertNotNull(editActivityImageDetails);
        assertEquals("ImagePath", editActivityImageDetails.getPath());
        assertEquals("ImageName", editActivityImageDetails.getName());
        assertEquals(new ComponentName(spyActivity, EditActivity.class), editActivityIntent.getComponent());
    }
}