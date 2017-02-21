package com.invisibleteam.goinvisible.util;

import android.content.Context;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ClearingInterval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SharedPreferencesUtilTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenIntervalIsNotStored_NullIntervalIsRestored() {
        //When
        ClearingInterval interval = SharedPreferencesUtil.getInterval(context);

        //Then
        assertThat(interval, is(nullValue()));
    }

    @Test
    public void whenIntervalIsStored_ProperIntervalIsRestored() {
        //Given
        SharedPreferencesUtil.saveInterval(context, ClearingInterval.DAY);

        //When
        ClearingInterval interval = SharedPreferencesUtil.getInterval(context);

        //Then
        assertThat(interval, is(ClearingInterval.DAY));
    }

    @Test
    public void whenCacheIsCleared_NullIntervalIsRestored() {
        //Given
        SharedPreferencesUtil.saveInterval(context, ClearingInterval.DAY);
        SharedPreferencesUtil.clearCache(context);

        //When
        ClearingInterval interval = SharedPreferencesUtil.getInterval(context);

        //Then
        assertThat(interval, is(nullValue()));
    }
}