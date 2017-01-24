package com.invisibleteam.goinvisible.mvvm.edition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditActivityTest {

    private Context context;

    @Before
    public void init() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenProperIntentIsPassed_ExtractionFinishWithSuccess() {
        //Given
        Intent intent = EditActivity.buildIntent(context, new ImageDetails("path", "name"));
        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(intent)
                .get();

        //When
        boolean isExtractionSucceed = activity.extractBundle();

        //Then
        assertTrue(isExtractionSucceed);
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
    public void whenProperIntentIsPassed_ViewModelIsInitiated() {
        //Given
        Intent intent = EditActivity.buildIntent(context, new ImageDetails("path", "name"));
        EditActivity activity = Robolectric
                .buildActivity(EditActivity.class)
                .withIntent(intent)
                .get();

        //When
        activity.onCreate(null);
        boolean isViewModelInitiated = activity.getEditViewModel() != null;

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
}
