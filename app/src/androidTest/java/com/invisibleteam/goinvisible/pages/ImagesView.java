package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.utilities.Config;

public class ImagesView extends UiView {

    // Main activity selectors
    private static final BySelector MAIN_ACTIVITY_SELECTOR = By.res(Config.GOINVISIBLE_PACKAGE, "activity_main");
    private static final BySelector NO_IMAGES_INFORMATION_SELECTOR = By.res(Config.GOINVISIBLE_PACKAGE, "no_images_information");

    public static Boolean isOpened() {
        return UI_DEVICE.wait(Until.hasObject(MAIN_ACTIVITY_SELECTOR), Config.LAUNCH_TIMEOUT) &&
                (isNoImagesInfoVisible() || isAnyImageLoaded());
    }

    public static Boolean isNoImagesInfoVisible() {
        return UI_DEVICE.hasObject(NO_IMAGES_INFORMATION_SELECTOR);
    }

    public static Boolean isAnyImageLoaded() {
        return UI_DEVICE.findObjects(By.res(GOINVISIBLE_PACKAGE, "image")).size() > 0;
    }
}
