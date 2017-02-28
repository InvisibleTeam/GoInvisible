package com.invisibleteam.goinvisible.mvvm.images.phone;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.ImagesViewCallback;

public interface PhoneImagesViewCallback extends ImagesViewCallback {
    void openEditScreen(ImageDetails imageDetails);
}
