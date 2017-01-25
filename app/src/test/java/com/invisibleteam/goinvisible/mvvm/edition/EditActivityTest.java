package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void whenOptionItemSelected_properMethodIsCalled() {
        //given
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).withIntent(editActivityIntent).get();
        EditActivity spyActivity = Mockito.spy(activity);
        spyActivity.onCreate(null);

        MenuItem menuItem = Mockito.mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);

        //when
        spyActivity.onOptionsItemSelected(menuItem);

        //then
        verify(spyActivity).onBackPressed();
    }

    @Test
    public void whenUnknownOptionItemSelected_defaultMethodIsCalled() {
        //given
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).withIntent(editActivityIntent).get();
        EditActivity spyActivity = Mockito.spy(activity);
        spyActivity.onCreate(null);

        MenuItem menuItem = Mockito.mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(-1);

        //when
        spyActivity.onOptionsItemSelected(menuItem);

        //then
        verify(spyActivity, times(0)).onBackPressed();
    }
}