package com.invisibleteam.goinvisible.mvvm.edition.callback;

import com.invisibleteam.goinvisible.mvvm.common.TagsUpdateStatusCallback;

public interface EditTagsUpdateStatusViewCallback extends RejectEditionChangesCallback, TagsUpdateStatusCallback {
    void navigateBack();

    void navigateChangedImagesScreen();

    /**
     * Change view from edit mode to default
     */
    void changeViewToDefaultMode();
}
