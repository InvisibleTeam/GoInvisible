package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.pages.UiDialog;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by nlesniewski on 14.02.2017.
 */

public class SystemHomeView extends UiDialog {

    public static Boolean isOpened() {
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        return mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT);
    }
}
