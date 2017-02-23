package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class TabletImagesViewModel extends ImagesViewModel {

    private TabletImagesViewCallback imagesViewCallback;

    TabletImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView, ImagesProvider imagesProvider, TabletImagesViewCallback imagesViewCallback) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        imagesViewCallback.showEditView(imageDetails);
    }

}
