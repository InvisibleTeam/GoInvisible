package com.invisibleteam.goinvisible.pages;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;

/**
 * Created by nlesniewski on 14.02.2017.
 */

public class GoInvisiblePackage extends UiDialog {

    public static void launch() {
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(GOINVISIBLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
    }

    public static Boolean isOpened() {
        return mDevice.wait(Until.hasObject(By.pkg(GOINVISIBLE_PACKAGE).depth(0)), TIMEOUT);
    }
}
