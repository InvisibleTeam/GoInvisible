package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.Config;

public class MissingPermissionsDialog extends UiView {

    // Missing permissions dialog selectors
    private static final BySelector MISSING_PERMISSIONS_TITLE_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "alertTitle");
    private static final BySelector MISSING_PERMISSIONS_MESSAGE_SELECTOR = By.res("android", "message");
    private static final BySelector EXIT_BUTTON_SELECTOR = By.res("android", "button2");
    private static final BySelector OK_BUTTON_SELECTOR = By.res("android", "button1");

    public static Boolean isOpened() {
        return UI_DEVICE.wait(Until.hasObject(MISSING_PERMISSIONS_TITLE_SELECTOR), Config.LAUNCH_TIMEOUT) &&
                UI_DEVICE.wait(Until.hasObject(MISSING_PERMISSIONS_MESSAGE_SELECTOR), Config.LAUNCH_TIMEOUT) &&
                UI_DEVICE.wait(Until.hasObject(EXIT_BUTTON_SELECTOR), Config.LAUNCH_TIMEOUT) &&
                UI_DEVICE.wait(Until.hasObject(OK_BUTTON_SELECTOR), Config.LAUNCH_TIMEOUT);
    }

    public static void exitApplication() {
        UI_DEVICE.findObject(EXIT_BUTTON_SELECTOR).click();
    }

    public static void ok() {
        UI_DEVICE.findObject(OK_BUTTON_SELECTOR).click();
    }
}
