package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class PhoneImagesViewModel extends ImagesViewModel {

    private PhoneImagesViewCallback imagesViewCallback;

    PhoneImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView, ImagesProvider imagesProvider, PhoneImagesViewCallback imagesViewCallback) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        imagesViewCallback.openEditScreen(imageDetails);
    }
}
