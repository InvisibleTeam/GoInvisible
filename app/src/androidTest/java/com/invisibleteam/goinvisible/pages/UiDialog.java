package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.UiDevice;

import com.invisibleteam.goinvisible.Config;
import com.invisibleteam.goinvisible.UiDeviceProvider;

public class UiDialog {
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String GOINVISIBLE_PACKAGE = Config.GOINVISIBLE_PACKAGE;

    protected static final int TIMEOUT = Config.LAUNCH_TIMEOUT;

    protected static final UiDevice UI_DEVICE = UiDeviceProvider.getInstance();
}
