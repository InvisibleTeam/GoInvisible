package com.invisibleteam.goinvisible.mvvm.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ClearingInterval;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.IntentMatcher.containsSameData;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SettingsActivityTest {

    private Context context;
    private SettingsActivity activity;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        activity = Robolectric.buildActivity(SettingsActivity.class).get();
    }

    @Test
    public void whenSettingsActivityBuildIsInitiated_SettingsActivityIntentIsCreated() {
        //Given

        Intent expectedIntent = new Intent(context, SettingsActivity.class);

        //When
        Intent intent = SettingsActivity.buildIntent(context);

        //Then
        assertThat(intent, containsSameData(expectedIntent));
    }

    @Test
    public void whenSettingsActivityIsCreatedWithoutSavedInterval_DefaultValuesAreSet() {
        //Given
        SettingsViewModel settingsViewModel = activity.getViewModel();

        //When
        activity.onCreate(null);

        //Then
        assertThat(settingsViewModel.getIntervalName().get(),
                is(ClearingInterval.DAY.getIntervalFormattedName(context)));
        assertTrue(!settingsViewModel.getIsClearServiceEnabled().get());
    }

    @Test
    public void whenSettingsActivityIsCreatedWithSavedInterval_ProperValueIsSet() {
        //Given
        SharedPreferencesUtil.saveInterval(context, ClearingInterval.WEEK);
        SharedPreferencesUtil.setClearingServiceActivation(context, true);
        SettingsViewModel settingsViewModel = activity.getViewModel();

        //When
        activity.onCreate(null);

        //Then
        assertThat(settingsViewModel.getIntervalName().get(),
                is(ClearingInterval.WEEK.getIntervalFormattedName(context)));
        assertTrue(settingsViewModel.getIsClearServiceEnabled().get());
    }

    @Test
    public void whenMenuItemIsClicked_OnBackButtonIsClicked() {
        //Given
        activity = spy(activity);
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        activity.onCreate(null);

        //When
        activity.onOptionsItemSelected(menuItem);

        //Then
        verify(activity).onBackPressed();
    }

    @Test
    public void whenOn3eClearIsCalled_wholeCacheisClearedAndIntervalIsResetToOneDay() {
        //Given
        activity.onCreate(null);
        SettingsViewModel.SettingsViewModelCallback callback = activity.getViewModelCallback();
        AlertDialog alertDialog = mock(AlertDialog.class);
        activity.setAlertDialog(alertDialog);

        //When
        callback.onOpenIntervalsDialog();

        //Then
        verify(alertDialog).show();
    }

    @Test
    public void whenEnableClearingServiceIsCalled_ServiceIsActivated() {
        //Given
        activity.onCreate(null);
        SettingsViewModel.SettingsViewModelCallback callback = activity.getViewModelCallback();

        //When
        callback.onEnableClearingService();

        //Then
        assertTrue(SharedPreferencesUtil.isClearingServiceActivated(context));
    }

    @Test
    public void whenDisableClearingServiceIsCalled_ServiceIsDeactivated() {
        //Given
        activity.onCreate(null);
        SettingsViewModel.SettingsViewModelCallback callback = activity.getViewModelCallback();

        //When
        callback.onDisableClearingService();

        //Then
        assertFalse(SharedPreferencesUtil.isClearingServiceActivated(context));
    }
}