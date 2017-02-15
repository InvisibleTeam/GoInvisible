package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.pages.UiDialog;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SystemHomeView extends UiDialog {

    public static Boolean isOpened() {
        final String launcherPackage = UI_DEVICE.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        return UI_DEVICE.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT);
    }

    public static void open() {
        UI_DEVICE.pressHome();
    }
}
