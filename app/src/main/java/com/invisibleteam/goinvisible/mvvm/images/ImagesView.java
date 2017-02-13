package com.invisibleteam.goinvisible.mvvm.images;

import com.invisibleteam.goinvisible.model.ImageDetails;

interface ImagesView {
    void navigateToEdit(ImageDetails imageDetails);

    void prepareSnackBar(int messageId);

    void showSnackBar();
}
