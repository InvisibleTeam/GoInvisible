package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.Config;

/**
 * Created by nlesniewski on 14.02.2017.
 */

public class MissingPermissionsDialog extends UiDialog {

    // Missing permissions dialog selectors
    private static final BySelector missingPermissionsTitleSelector = By.res(GOINVISIBLE_PACKAGE, "alertTitle");
    private static final BySelector missingPermissionsMessageSelector = By.res("android", "message");
    private static final BySelector exitButtonSelector = By.res("android", "button2");
    private static final BySelector okButtonSelector = By.res("android", "button1");

    public static Boolean isOpened() {
        return mDevice.wait(Until.hasObject(missingPermissionsTitleSelector), Config.LAUNCH_TIMEOUT) &&
                mDevice.wait(Until.hasObject(missingPermissionsMessageSelector), Config.LAUNCH_TIMEOUT) &&
                mDevice.wait(Until.hasObject(exitButtonSelector), Config.LAUNCH_TIMEOUT) &&
                mDevice.wait(Until.hasObject(okButtonSelector), Config.LAUNCH_TIMEOUT);
    }

    public static void exitApplication() {
        mDevice.findObject(exitButtonSelector).click();
    }

    public static void ok() {
        mDevice.findObject(okButtonSelector).click();
    }
}
