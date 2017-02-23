package com.invisibleteam.goinvisible.mvvm.edition;

interface EditMenuViewCallback {
    void showRejectChangesDialog();

    void navigateBack();

    void navigateChangedImagesScreen();

    void showTagsSuccessfullyUpdatedMessage();

    void showTagsUpdateFailureMessage();

    /**
     * Change view from edit mode to default
     */
    void changeViewToDefaultMode();
}
