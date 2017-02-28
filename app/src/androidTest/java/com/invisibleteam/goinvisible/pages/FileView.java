package com.invisibleteam.goinvisible.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;

import com.invisibleteam.goinvisible.utilities.UiScrollable2;

import java.util.LinkedHashMap;

import static com.invisibleteam.goinvisible.utilities.Config.GOINVISIBLE_PACKAGE;
import static com.invisibleteam.goinvisible.utilities.Config.LAUNCH_TIMEOUT;
import static com.invisibleteam.goinvisible.utilities.Config.UI_DEVICE;

public class FileView {
    private static final BySelector DETAILS_BACKDROP_GRADIENT_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "details_backdrop_gradient");
    private static final BySelector RECYCLER_VIEW_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "recycler");

    private static final BySelector TAG_KEY_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "tag_key");
    private static final BySelector TAG_VALUE_SELECTOR = By.res(GOINVISIBLE_PACKAGE, "tag_value");
    private static final BySelector VISIBLE_LIST_ITEM_SELECTOR = By.clazz("android.widget.RelativeLayout").hasChild(TAG_KEY_SELECTOR);

    public static boolean isOpened() {
        return UI_DEVICE.wait(Until.hasObject(DETAILS_BACKDROP_GRADIENT_SELECTOR), LAUNCH_TIMEOUT);
    }

    public static LinkedHashMap<String, String> getExifData() throws UiObjectNotFoundException {
        LinkedHashMap<String, String> searchResult = new LinkedHashMap<>();

        UiScrollable2 exifTagsList = new UiScrollable2(UI_DEVICE.findObject(RECYCLER_VIEW_SELECTOR));

        //noinspection SpellCheckingInspection
        boolean scrollResult = true;
        boolean scrollResultPrevious;

        do {
            for (UiObject2 uiListElement : exifTagsList.getUiObject2().findObjects(VISIBLE_LIST_ITEM_SELECTOR)) {
                searchResult.put(
                        uiListElement.findObject(TAG_KEY_SELECTOR).getText(),
                        uiListElement.findObject(TAG_VALUE_SELECTOR).getText()
                );
            }

            scrollResultPrevious = scrollResult;

            scrollResult = exifTagsList.scrollPage(Direction.DOWN);
        } while (scrollResult || scrollResultPrevious);

        exifTagsList.scrollToBound(Direction.UP);

        return searchResult;
    }
}
