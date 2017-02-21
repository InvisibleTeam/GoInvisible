package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import static com.invisibleteam.goinvisible.utilities.Config.LAUNCH_TIMEOUT;
import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;

public class SystemPermissionDialog {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String PACKAGE_INSTALLER_PACKAGE = "com.android.packageinstaller";

    // Permission Dialog selectors
    private static final BySelector PERMISSION_MESSAGE_SELECTOR = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_message");
    private static final BySelector DO_NOT_ASK_AGAIN_SELECTOR = By.res(PACKAGE_INSTALLER_PACKAGE, "do_not_ask_checkbox");
    private static final BySelector DENY_BUTTON_TEXT_SELECTOR = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_deny_button");
    private static final BySelector ALLOW_BUTTON_TEXT_SELECTOR = By.res(PACKAGE_INSTALLER_PACKAGE, "permission_allow_button");

    public static Boolean isOpened() {
        return UI_DEVICE.wait(Until.hasObject(PERMISSION_MESSAGE_SELECTOR), LAUNCH_TIMEOUT);
    }

    public static void allowAccess() {
        UI_DEVICE.wait(Until.findObject(ALLOW_BUTTON_TEXT_SELECTOR), LAUNCH_TIMEOUT);
        UI_DEVICE.findObject(ALLOW_BUTTON_TEXT_SELECTOR).click();
    }

    public static void denyAccess() {
        UI_DEVICE.wait(Until.hasObject(DENY_BUTTON_TEXT_SELECTOR), LAUNCH_TIMEOUT);
        UI_DEVICE.findObject(DENY_BUTTON_TEXT_SELECTOR).click();
    }

    public static Boolean doNotAskAgain(boolean isChecked) {
        UiObject2 checkBox = UI_DEVICE.wait(Until.findObject(DO_NOT_ASK_AGAIN_SELECTOR), LAUNCH_TIMEOUT);
        if (checkBox.isChecked() != isChecked) {
            UI_DEVICE.findObject(DO_NOT_ASK_AGAIN_SELECTOR).click();
        }
        return checkBox.isChecked();
    }
}
