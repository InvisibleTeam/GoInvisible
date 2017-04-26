package com.invisibleteam.goinvisible.mvvm.images.tablet;


import android.os.Bundle;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.TagChangesStatusProvider;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.mvvm.images.ImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

import javax.annotation.Nullable;

public class TabletImagesViewModel extends ImagesViewModel {

    private static final String IMAGE_DETAILS_EXTRA_KEY = "image_details_extra";

    private final TabletImagesViewCallback imagesViewCallback;
    private final TagChangesStatusProvider statusProvider;
    private
    @Nullable
    ImageDetails chosenImage = null;

    public TabletImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                                 ImagesProvider imagesProvider,
                                 TabletImagesViewCallback callback,
                                 TagChangesStatusProvider statusProvider) {
        super(imagesCompoundRecyclerView, imagesProvider, callback);
        this.imagesViewCallback = callback;
        this.statusProvider = statusProvider;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        chosenImage = imageDetails;
        if (statusProvider.isInEditState()) {
            imagesViewCallback.showRejectChangesDialog();
        } else {
            imagesViewCallback.showEditView(imageDetails);
        }
    }

    public void onRejectTagsChangesDialogPositive() {
        if (chosenImage != null) {
            imagesViewCallback.showEditView(chosenImage);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(IMAGE_DETAILS_EXTRA_KEY, chosenImage);
    }

    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        if (bundle == null) {
            return;
        }

        if (bundle.containsKey(IMAGE_DETAILS_EXTRA_KEY)) {
            chosenImage = bundle.getParcelable(IMAGE_DETAILS_EXTRA_KEY);
        }
    }
}
