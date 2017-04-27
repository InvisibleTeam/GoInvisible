package com.invisibleteam.goinvisible.mvvm.images.tablet;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.callback.RejectEditionChangesCallback;
import com.invisibleteam.goinvisible.mvvm.images.ImagesViewCallback;

public interface TabletImagesViewCallback extends ImagesViewCallback, RejectEditionChangesCallback {
    void showEditView(ImageDetails imageDetails);
}
