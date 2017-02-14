package com.invisibleteam.goinvisible.pages;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

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
}
