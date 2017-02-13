package com.invisibleteam.goinvisible.mvvm.images;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImagesCallbackTest {

    @Test
    public void whenSnackBarIsCreated_ItsnotcreatedAgain() {
        //Given
        Activity activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
        ImagesCallback imagesCallback = new ImagesCallback(activity);
        imagesCallback.prepareSnackBar(R.string.error);

        Snackbar baseSnackbar = imagesCallback.getSnackbar();

        //When
        imagesCallback.prepareSnackBar(R.string.error);

        //Then
        Snackbar finalSnackbar = imagesCallback.getSnackbar();
        assertEquals(baseSnackbar, finalSnackbar);
    }
}
