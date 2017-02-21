package com.invisibleteam.goinvisible.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.media.ExifInterface;
import android.util.Log;

import com.invisibleteam.goinvisible.GoInvisibleApplication;
import com.invisibleteam.goinvisible.model.ClearingInterval;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.List;

public class ClearingTagsService extends Service {

    private static final String TAG = ClearingTagsService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        ClearingInterval interval = SharedPreferencesUtil.getInterval(this);
        if (interval == null) {
            stopCyclicTagsClearing();
            return START_NOT_STICKY;
        }

        List<ImageDetails> imageDetailsList = getImages();

        for (ImageDetails imageDetails : imageDetailsList) {
            performTagsClearing(imageDetails);
        }

        return START_STICKY;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void performTagsClearing(ImageDetails imageDetails) {
        try {
            TagsManager tagsManager = new TagsManager(getExifInterface(imageDetails));
            tagsManager.clearTags(tagsManager.getAllTags());
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    ExifInterface getExifInterface(ImageDetails imageDetails) throws IOException {
        return new ExifInterface(imageDetails.getPath());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ImageDetails> getImages() {
        ImagesProvider imagesProvider = new ImagesProvider(getContentResolver());
        return imagesProvider.getImagesList();
    }

    private void stopCyclicTagsClearing() {
        GoInvisibleApplication.stopClearingServiceAlarm(this);
        stopSelf();
    }
}
