package com.invisibleteam.goinvisible.mvvm.edition;

public interface EditMenuViewCallback extends EditTagsTabletCallback, RejectEditionChangesCallback{

    void navigateBack();

    void navigateChangedImagesScreen();

    /**
     * Change view from edit mode to default
     */
    void changeViewToDefaultMode();
}
