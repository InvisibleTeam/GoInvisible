package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.utilities.Config;

import java.util.List;

import static com.invisibleteam.goinvisible.utilities.Config.GOINVISIBLE_PACKAGE;
import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;

public class ImagesView {

    // Main activity selectors
    private static final BySelector MAIN_ACTIVITY_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "activity_main");
    private static final BySelector NO_IMAGES_INFORMATION_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "no_images_information");

    /**
     * IMAGE_SELECTOR will return:
     * - first image when findObject is used
     * - all images when findObjects is used
     */
    private static final BySelector IMAGE_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "image");

    private static final BySelector RECYCLER_ELEMENT_SELECTOR =
            By.clazz("android.widget.RelativeLayout").pkg(GOINVISIBLE_PACKAGE).hasChild(IMAGE_SELECTOR);

    public static Boolean isOpened() {
        return UI_DEVICE.wait(Until.hasObject(MAIN_ACTIVITY_SELECTOR), Config.LAUNCH_TIMEOUT) &&
                (isNoImagesInfoVisible() || isAnyImageLoaded());
    }

    public static Boolean isNoImagesInfoVisible() {
        return UI_DEVICE.hasObject(NO_IMAGES_INFORMATION_SELECTOR);
    }

    public static Boolean isAnyImageLoaded() {
        return UI_DEVICE.findObjects(IMAGE_SELECTOR).size() > 0;
    }

    public static List<UiObject2> getVisibleImages() {
        return UI_DEVICE.findObjects(RECYCLER_ELEMENT_SELECTOR);
    }

    public static void pullToRefresh() {
        int centerOfWidth = UI_DEVICE.getDisplayWidth() / 2;
        UI_DEVICE.drag(centerOfWidth, UI_DEVICE.getDisplayHeight() / 3, centerOfWidth, UI_DEVICE.getDisplayHeight() / 2, 6);
    }
}
