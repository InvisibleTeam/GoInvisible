package com.invisibleteam.goinvisible.utilities;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiObject2;

public class UiScrollable2 {
    private final UiObject2 mUiObject2;

    public UiScrollable2(UiObject2 uiObject2) {
        mUiObject2 = uiObject2;
    }

    public boolean scrollPage(Direction direction) {
        return mUiObject2.scroll(direction, 1.0f);
    }

    public void scrollToBound(Direction direction) {
        do {
        } while (mUiObject2.scroll(direction, Float.MAX_VALUE, Integer.MAX_VALUE));
    }

    public boolean scrollToText(String text) {
        return scrollTo(By.text(text));
    }

    public boolean scrollTo(BySelector by) {
        boolean searchResult = false;

        scrollToBound(Direction.UP);

        //noinspection SpellCheckingInspection
        boolean scrollResult = true;
        boolean scrollResultPrevious;

        do {
            if (mUiObject2.hasObject(by)) {
                searchResult = true;
                break;
            }

            scrollResultPrevious = scrollResult;

            scrollResult = scrollPage(Direction.DOWN);
        } while (scrollResult || scrollResultPrevious);

        return searchResult;
    }

    public UiObject2 getUiObject2() {
        return mUiObject2;
    }
}
