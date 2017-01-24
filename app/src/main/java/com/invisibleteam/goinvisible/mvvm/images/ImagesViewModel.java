package com.invisibleteam.goinvisible.mvvm.images;


import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class ImagesViewModel {

    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;
    private ImagesProvider imagesProvider;
    private ImagesView imagesView;
    private boolean isInitialized;

    ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                           ImagesProvider imagesProvider,
                           ImagesView imagesView) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;
        this.imagesProvider = imagesProvider;
        this.imagesView = imagesView;

        initRecyclerView();
    }

    private void initRecyclerView() {
        imagesCompoundRecyclerView.updateResults(imagesProvider.getImagesList());
        imagesCompoundRecyclerView.setOnItemClickListener(imageDetails -> imagesView.navigateToEdit(imageDetails));
        isInitialized = true;
    }

    @VisibleForTesting
    boolean isInitialized() {
        return isInitialized;
    }
}
