package com.invisibleteam.goinvisible.mvvm.settings;

import com.invisibleteam.goinvisible.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SettingsViewModelTest {

    private SettingsViewModel.SettingsViewModelCallback callback;
    private SettingsViewModel viewModel;

    @Before
    public void setUp() {
        callback = mock(SettingsViewModel.SettingsViewModelCallback.class);
        viewModel = new SettingsViewModel(callback);
    }

    @Test
    public void whenIntervalsSwitchIsChecked_EnableClearingServiceIsCalled() {
        //When
        viewModel.onCheckedChanged(true);

        //Then
        verify(callback).onEnableClearingService();
        assertTrue(viewModel.getIsClearServiceEnabled().get());
    }

    @Test
    public void whenIntervalsSwitchIsUnchecked_ClearingCacheIsCalled() {
        //When
        viewModel.onCheckedChanged(false);

        //Then
        verify(callback).onDisableClearingService();
        assertFalse(viewModel.getIsClearServiceEnabled().get());
    }

    @Test
    public void whenIntervalsLayoutIsClicked_OpenIntervalsDialogIsCalled() {
        //When
        viewModel.onClick();

        //Then
        verify(callback).onOpenIntervalsDialog();
    }
}