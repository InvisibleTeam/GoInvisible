package com.invisibleteam.goinvisible.pages;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import com.invisibleteam.goinvisible.pages.system.SystemHomeView;

import static org.junit.Assert.assertTrue;

public class GoInvisiblePackage extends UiView {

    public static void launch() {
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(GOINVISIBLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
    }

    public static void launchFromHomeScreen() {
        // Start from the home screen
        SystemHomeView.open();

        // Wait for launcher
        assertTrue(SystemHomeView.isOpened());

        // Launch the GoInvisible app
        GoInvisiblePackage.launch();
    }
}
