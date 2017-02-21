package com.invisibleteam.goinvisible.pages;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.io.Files;
import android.util.Log;

import com.invisibleteam.goinvisible.pages.system.SystemHomeView;
import com.invisibleteam.goinvisible.utilities.ShellCmdHelper;
import com.invisibleteam.goinvisible.utilities.StorageManagementHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.invisibleteam.goinvisible.utilities.Config.GOINVISIBLE_PACKAGE;
import static com.invisibleteam.goinvisible.utilities.StorageManagementHelper.updateDeviceMediaList;

import static org.junit.Assert.assertTrue;

public class GoInvisibleApplication {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String GOINVISIBLE_PACKAGE_TEST = "com.invisibleteam.goinvisible.test";

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
        GoInvisibleApplication.launch();
    }

    public static void grantStoragePermissions() {
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE_TEST, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE_TEST, Manifest.permission.READ_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
