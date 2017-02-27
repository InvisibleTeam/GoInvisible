package com.invisibleteam.goinvisible.mvvm.images.callback;

import com.invisibleteam.goinvisible.model.ImageDetails;

public interface PhoneImagesViewCallback extends ImagesViewCallback {
    void openEditScreen(ImageDetails imageDetails);
}
