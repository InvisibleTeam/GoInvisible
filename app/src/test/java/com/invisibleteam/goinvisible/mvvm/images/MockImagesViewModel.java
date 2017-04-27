package com.invisibleteam.goinvisible.mvvm.images;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

class MockImagesViewModel extends ImagesViewModel {

    MockImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView, ImagesProvider imagesProvider, ImagesViewCallback
            callback) {
        super(imagesCompoundRecyclerView, imagesProvider, callback);
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
    }
}
