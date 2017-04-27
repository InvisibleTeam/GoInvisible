package com.invisibleteam.goinvisible.mvvm.common;

import android.app.Dialog;
import android.content.Context;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RadioDialogBuilderTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void WhenBuildIsCalled_DialogIsCreated() {
        //Given
        RadioDialogBuilder.RadioDialogCallback callback = mock(RadioDialogBuilder.RadioDialogCallback.class);
        String[] values = new String[]{"1", "2", "3"};
        RadioDialogBuilder radioDialogBuilder = new RadioDialogBuilder(context, values, 1, callback);
        radioDialogBuilder.setPositiveButton("positive");
        radioDialogBuilder.setNegativeButton("negative");

        //When
        Dialog dialog = radioDialogBuilder.build();

        //Then
        assertNotNull(dialog);
    }

}

