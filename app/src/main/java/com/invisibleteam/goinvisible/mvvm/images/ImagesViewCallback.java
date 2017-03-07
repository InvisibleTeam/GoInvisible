package com.invisibleteam.goinvisible.mvvm.images;

import android.support.annotation.StringRes;

public interface ImagesViewCallback {
    void prepareSnackBar(@StringRes int messageResId);

    void showSnackBar();

    void onStopRefreshingImages();
}
