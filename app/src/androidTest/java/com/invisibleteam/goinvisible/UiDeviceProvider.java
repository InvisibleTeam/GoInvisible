package com.invisibleteam.goinvisible;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

public class UiDeviceProvider {
    private static UiDevice ourInstance = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    private UiDeviceProvider() {
    }

    public static UiDevice getInstance() {
        return ourInstance;
    }
}
