package com.invisibleteam.goinvisible.mvvm.images;


import android.content.ContentResolver;

import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

class ImagesViewModel {
    private ImagesCompoundRecyclerView imagesCompoundRecyclerView;
    private ContentResolver contentResolver;

    ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView, ContentResolver contentResolver) {
        this.imagesCompoundRecyclerView = imagesCompoundRecyclerView;
        this.contentResolver = contentResolver;

        init();
    }

    private void init() {
        ImagesProvider imagesProvider = new ImagesProvider(contentResolver);
        imagesCompoundRecyclerView.updateResults(imagesProvider.getImagesList());
    }
}
