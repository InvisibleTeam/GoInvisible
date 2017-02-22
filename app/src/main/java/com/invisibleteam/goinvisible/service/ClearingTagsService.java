package com.invisibleteam.goinvisible.service;


import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.media.ExifInterface;
import android.util.Log;

import com.invisibleteam.goinvisible.helper.ClearingTagsReceiverHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.List;

public class ClearingTagsService extends IntentService {

    private static final String TAG = ClearingTagsService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClearingTagsService(String name) {
        super(name);
    }

    public ClearingTagsService() {
        super(ClearingTagsService.class.getName());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isServiceEnabled = SharedPreferencesUtil.isClearingServiceActivated(this);
        if (!isServiceEnabled) {
            stopCyclicTagsClearing();
            return;
        }

        List<ImageDetails> imageDetailsList = getImages();

        for (ImageDetails imageDetails : imageDetailsList) {
            performTagsClearing(imageDetails);
        }
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
        ClearingTagsReceiverHelper.stopClearingServiceAlarm(this);
        stopSelf();
    }
}
