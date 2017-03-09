package com.invisibleteam.goinvisible.helper;

import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.FileNotFoundException;

import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditActivityHelperTest {

    private EditActivity activity;
    private EditActivityHelper helper;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        Intent editActivityIntent = EditActivity.buildIntent(context, imageDetails);
        activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(editActivityIntent)
                .create()
                .get();
        helper = new EditActivityHelper(activity);
        helper = spy(helper);
    }

    @Ignore
    @Test
    public void whenShareImageIntentIsBuildWithCorrectImagePath_IntentIsReturned() throws FileNotFoundException {
        //Given
        /*ImageDetails imageDetails = new ImageDetails("Path", "Name");
        doReturn("media:content").when(helper).prepareImagePathToShare(imageDetails, activity.getContentResolver());
        Intent expectedIntent = new Intent(Intent.ACTION_SEND);
        expectedIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("media:content"));
        expectedIntent.setType("image/jpg");*/

        //When
        //Intent shareIntent = helper.buildShareImageIntent(imageDetails, activity.getContentResolver());

        //Then
        /*assertThat(shareIntent, is(notNullValue()));
        assertThat(shareIntent.getType(), is("image/jpg"));
        assertThat(shareIntent.getExtras().get(Intent.EXTRA_STREAM), is(Uri.parse("media:content")));*/
    }

    @Ignore
    @Test(expected = FileNotFoundException.class)
    public void whenShareImageIntentIsBuildWithIncorrectImagePath_ExceptionIsThrown() throws FileNotFoundException {
        //Given
        //ImageDetails imageDetails = new ImageDetails("Path", "Name");

        //When
        //helper.buildShareImageIntent(imageDetails, activity.getContentResolver());
    }

}