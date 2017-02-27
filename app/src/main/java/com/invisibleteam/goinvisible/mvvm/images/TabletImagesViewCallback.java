package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.RejectEditionChangesCallback;

public interface TabletImagesViewCallback extends ImagesViewCallback, RejectEditionChangesCallback {
    void showEditView(ImageDetails imageDetails);

    void changeViewToDefaultMode();
}
