package com.invisibleteam.goinvisible.utilities;

import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.io.Files;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StorageManagementHelper {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CAMERA_OUTPUT_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";

    public static final String PICTURES_OUTPUT_PATH = Environment.getExternalStorageDirectory().getPath() + "/Pictures";

    public static void deleteImagesFromPhotoDirectory() {
        StorageManagementHelper.deleteImagesFromDirectory(CAMERA_OUTPUT_DIRECTORY);
    }

    public static void deleteAssetsFromLocalStorage() {
        StorageManagementHelper.deleteImagesFromDirectory(PICTURES_OUTPUT_PATH);
    }

    public static void copyAssetsToLocalStorage() {
        AssetManager assetManager = InstrumentationRegistry.getTargetContext().getAssets();
        String[] filesList = null;
        String inputPath = "testData";

        try {
            filesList = assetManager.list(inputPath);
        } catch (IOException e) {
            Log.e("StorageManagementHelper", "Failed to get asset file list.", e);
        }

        for (String fileName : filesList) {
            try {
                InputStream stream = assetManager.open(inputPath + "/" + fileName);

                byte[] data = new byte[stream.available()];
                stream.read(data);

                File outFile = new File(PICTURES_OUTPUT_PATH, fileName);
                if (!outFile.exists()) {
                    Files.touch(outFile);
                    Files.write(data, outFile);

                    updateDeviceMediaList(outFile.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.e("StorageManagementHelper", "Failed to copy asset file: " + fileName, e);
            }
        }
    }

    /**
     * The goal of
     * @param directoryPath path to directory with images
     */
    public static void deleteImagesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        for (File file : directory.listFiles()) {
            file.delete();
            updateDeviceMediaList(file.getAbsolutePath());
        }
    }

    public static void updateDeviceMediaList(String filePath) {
        MediaScannerConnection.scanFile(
                InstrumentationRegistry.getTargetContext(),
                new String[]{filePath},
                null,
                (path, uri) -> Log.i("copyAssetsToStorage", String.format("Scanned %s: uri=%s", path, uri)));
    }

}
