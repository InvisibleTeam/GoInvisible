package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;

public interface TabletImagesViewCallback extends ImagesViewCallback {
    void showEditView(ImageDetails imageDetails);
}
