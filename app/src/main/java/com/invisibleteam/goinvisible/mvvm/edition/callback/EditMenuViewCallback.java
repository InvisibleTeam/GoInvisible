package com.invisibleteam.goinvisible.mvvm.edition.callback;

import com.invisibleteam.goinvisible.mvvm.common.MenuCallback;

public interface EditMenuViewCallback extends RejectEditionChangesCallback, MenuCallback {
    void navigateBack();

    void navigateChangedImagesScreen();

    /**
     * Change view from edit mode to default
     */
    void changeViewToDefaultMode();
}
