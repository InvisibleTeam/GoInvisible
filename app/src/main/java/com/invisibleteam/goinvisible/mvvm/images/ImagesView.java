package com.invisibleteam.goinvisible.mvvm.images;

import android.widget.ImageView;

import com.invisibleteam.goinvisible.model.ImageDetails;

interface ImagesView {
    void navigateToEdit(ImageView view, ImageDetails imageDetails);

    void showUnsupportedImageInfo();
}
