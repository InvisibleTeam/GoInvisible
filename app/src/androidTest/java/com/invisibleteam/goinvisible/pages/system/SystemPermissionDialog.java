package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.pages.UiDialog;

import static com.invisibleteam.goinvisible.Config.LAUNCH_TIMEOUT;

public class SystemPermissionDialog extends UiDialog {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String PACKAGE_INSTALLER_PACKAGE = "com.android.packageinstaller";

    // Permission Dialog selectors
    private static final BySelector permissionMessageSelector = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_message");
    private static final BySelector doNotAskAgainSelector = By.res(PACKAGE_INSTALLER_PACKAGE, "do_not_ask_checkbox");
    private static final BySelector denyButtonTextSelector = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_deny_button");
    private static final BySelector allowButtonTextSelector = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_allow_button");

    public static Boolean isOpened() {
        return mDevice.wait(Until.hasObject(permissionMessageSelector), LAUNCH_TIMEOUT);
    }

    public static void allowAccess() {
        mDevice.wait(Until.findObject(allowButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(allowButtonTextSelector).click();
    }

    public static void denyAccess() {
        mDevice.wait(Until.hasObject(denyButtonTextSelector), LAUNCH_TIMEOUT);
        mDevice.findObject(denyButtonTextSelector).click();
    }

    public static Boolean doNotAskAgain(boolean isChecked) {
        UiObject2 checkBox = mDevice.wait(Until.findObject(doNotAskAgainSelector), LAUNCH_TIMEOUT);
        if (checkBox.isChecked() != isChecked) {
            mDevice.findObject(doNotAskAgainSelector).click();
        }
        return checkBox.isChecked();
    }
}
