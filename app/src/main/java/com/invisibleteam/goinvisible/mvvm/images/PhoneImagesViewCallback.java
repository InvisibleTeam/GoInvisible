package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;

public interface PhoneImagesViewCallback extends ImagesViewCallback {
    void openEditScreen(ImageDetails imageDetails);
}
