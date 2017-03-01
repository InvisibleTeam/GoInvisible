package com.invisibleteam.goinvisible.mvvm.images;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.StringRes;

public interface ImagesViewCallback {
    void prepareSnackBar(@StringRes int messageResId);

    void showSnackBar();

    void onStopRefreshingImages();

    @TargetApi(Build.VERSION_CODES.N)
    default int getGet() {
        return 0;
    }
}
