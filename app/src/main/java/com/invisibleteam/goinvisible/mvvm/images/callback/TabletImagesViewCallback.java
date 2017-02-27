package com.invisibleteam.goinvisible.mvvm.images.callback;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.callback.RejectEditionChangesCallback;

public interface TabletImagesViewCallback extends ImagesViewCallback, RejectEditionChangesCallback {
    void showEditView(ImageDetails imageDetails);

    void changeViewToDefaultMode();
}
