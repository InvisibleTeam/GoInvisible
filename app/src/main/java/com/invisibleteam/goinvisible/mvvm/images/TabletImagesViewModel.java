package com.invisibleteam.goinvisible.mvvm.images;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

import javax.annotation.Nullable;

class TabletImagesViewModel extends ImagesViewModel {

    private TabletImagesViewCallback imagesViewCallback;
    private EditCompoundRecyclerView editCompoundRecyclerView;
    private @Nullable ImageDetails lastChosenImage = null;

    TabletImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                          ImagesProvider imagesProvider,
                          TabletImagesViewCallback imagesViewCallback,
                          EditCompoundRecyclerView editCompoundRecyclerView) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
        this.editCompoundRecyclerView = editCompoundRecyclerView;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        lastChosenImage = imageDetails;
        if (!editCompoundRecyclerView.getChangedTags().isEmpty()) {
            imagesViewCallback.showRejectChangesDialog();
        } else {
            imagesViewCallback.showEditView(imageDetails);
        }
    }

    void onRejectTagsChangesDialogPositive() {
        if (lastChosenImage != null) {
            imagesViewCallback.showEditView(lastChosenImage);
            imagesViewCallback.changeViewToDefaultMode();
        }
    }
}
