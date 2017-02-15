package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.Config;

public class FileView extends UiDialog {

    // Main activity selectors
    private static final BySelector mainActivitySelector = By.res(Config.GOINVISIBLE_PACKAGE, "activity_main");
    private static final BySelector noImagesInformationSelector = By.res(Config.GOINVISIBLE_PACKAGE, "no_images_information");

    public static Boolean isOpened() {
        return mDevice.wait(Until.hasObject(mainActivitySelector), Config.LAUNCH_TIMEOUT) &&
                mDevice.wait(Until.hasObject(noImagesInformationSelector), Config.LAUNCH_TIMEOUT);
    }
}
