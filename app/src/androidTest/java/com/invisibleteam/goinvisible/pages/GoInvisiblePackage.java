package com.invisibleteam.goinvisible.pages;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.io.Files;
import android.util.Log;

import com.invisibleteam.goinvisible.pages.system.SystemHomeView;
import com.invisibleteam.goinvisible.utilities.ShellCmdHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class GoInvisiblePackage extends UiView {

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
        GoInvisiblePackage.launch();
    }

    public static void copyAssetsToLocalStorage() {
        AssetManager assetManager = InstrumentationRegistry.getTargetContext().getAssets();
        String[] filesList = null;
        String inputPath = "testData";
        String outputPath = "/sdcard/Pictures";

        try {
            filesList = assetManager.list(inputPath);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        for (String fileName : filesList) {
            try {
                InputStream stream = assetManager.open(inputPath + "/" + fileName);

                byte data[] = new byte[stream.available()];
                stream.read(data);

                File outFile = new File(outputPath, fileName);
                if (!outFile.exists()) {
                    Files.touch(outFile);
                    Files.write(data, outFile);

                    MediaScannerConnection.scanFile(
                            InstrumentationRegistry.getTargetContext(),
                            new String[]{outFile.getAbsolutePath()},
                            null,
                            (path, uri) -> Log.i("copyAssetsToStorage", String.format("Scanned %s: uri=%s", path, uri)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void grantStoragePermissions() {
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE_TEST, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE_TEST, Manifest.permission.READ_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ShellCmdHelper.grantPermission(GOINVISIBLE_PACKAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
