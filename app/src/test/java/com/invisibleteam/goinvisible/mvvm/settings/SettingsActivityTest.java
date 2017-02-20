package com.invisibleteam.goinvisible.mvvm.settings;

import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.IntentMatcher.containsSameData;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SettingsActivityTest {

    @Test
    public void whenSettingsActivityBuildIsInitiated_SettingsActivityIntentIsCreated() {
        //Given
        Context context = RuntimeEnvironment.application;
        Intent expectedIntent = new Intent(context, SettingsActivity.class);

        //When
        Intent intent = SettingsActivity.buildIntent(context);

        //Then
        assertThat(intent, containsSameData(expectedIntent));
    }
}