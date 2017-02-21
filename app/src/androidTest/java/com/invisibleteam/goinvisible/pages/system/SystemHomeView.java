package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;


import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;
import static com.invisibleteam.goinvisible.utilities.Config.LAUNCH_TIMEOUT;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SystemHomeView {

    public static Boolean isOpened() {
        final String launcherPackage = UI_DEVICE.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        return UI_DEVICE.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    public static void open() {
        UI_DEVICE.pressHome();
    }
}
