package com.invisibleteam.goinvisible.pages.system;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.utilities.ShellCmdHelper;

import static com.invisibleteam.goinvisible.utilities.Config.LAUNCH_TIMEOUT;
import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;

public class CameraView {

    private static final BySelector SHUTTER_BUTTON_SELECTOR = By.res("com.android.camera", "shutter_button");
    private static final BySelector THUMBNAIL_VIEW_SELECTOR = By.res("com.android.camera", "thumbnail");

    public static void takePhoto() {
        ShellCmdHelper.startCamera();

        UI_DEVICE.wait(Until.hasObject(SHUTTER_BUTTON_SELECTOR), LAUNCH_TIMEOUT);
        UI_DEVICE.findObject(SHUTTER_BUTTON_SELECTOR).click();

        //it is weak condition when there is any photo
        UI_DEVICE.wait(Until.hasObject(THUMBNAIL_VIEW_SELECTOR), LAUNCH_TIMEOUT);
    }
}
