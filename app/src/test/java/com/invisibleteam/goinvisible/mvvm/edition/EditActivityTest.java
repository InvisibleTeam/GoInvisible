package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
public class EditActivityTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenActivityIsStartedWithIntentAndIntentContainsData_thenDataIsFetchedCorrectly() {
        //given
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);

        //when
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).withIntent(editActivityIntent).create().get();

        //then
        assertNotNull(activity.getImageDetails());
        assertEquals("Path", activity.getImageDetails().getPath());
        assertEquals("Name", activity.getImageDetails().getName());
    }
}