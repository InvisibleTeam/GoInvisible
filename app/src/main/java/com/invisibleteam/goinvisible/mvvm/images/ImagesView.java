package com.invisibleteam.goinvisible.mvvm.images;

import android.support.annotation.StringRes;

import com.invisibleteam.goinvisible.model.ImageDetails;

interface ImagesView {
    void navigateToEdit(ImageDetails imageDetails);

    void prepareSnackBar(@StringRes int messageResId);

    void showSnackBar();

    void onStopRefreshingImages();
}
