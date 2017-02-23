package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class Tablet2ImagesViewModel extends ImagesViewModel {

    private TabletImagesViewCallback imagesViewCallback;

    Tablet2ImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView, ImagesProvider imagesProvider, TabletImagesViewCallback imagesViewCallback) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        imagesViewCallback.showEditView(imageDetails);
    }

}
