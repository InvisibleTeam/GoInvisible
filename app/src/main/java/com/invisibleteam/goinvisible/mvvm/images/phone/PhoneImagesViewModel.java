package com.invisibleteam.goinvisible.mvvm.images.phone;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.mvvm.images.ImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

public class PhoneImagesViewModel extends ImagesViewModel {

    private PhoneImagesViewCallback imagesViewCallback;

    public PhoneImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                         ImagesProvider imagesProvider,
                         PhoneImagesViewCallback imagesViewCallback) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        imagesViewCallback.openEditScreen(imageDetails);
    }
}
