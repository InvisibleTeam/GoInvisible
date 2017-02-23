package com.invisibleteam.goinvisible.utilities;

import android.support.test.uiautomator.UiDevice;

public class Config {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String GOINVISIBLE_PACKAGE = "com.invisibleteam.goinvisible";

    public static final int LAUNCH_TIMEOUT = 5000;

    public static final UiDevice UI_DEVICE = UiDeviceProvider.getInstance();

}
