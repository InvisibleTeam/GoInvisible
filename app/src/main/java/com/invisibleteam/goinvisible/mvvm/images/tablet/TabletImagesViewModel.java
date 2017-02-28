package com.invisibleteam.goinvisible.mvvm.images.tablet;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.images.ImagesProvider;
import com.invisibleteam.goinvisible.mvvm.images.ImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImagesCompoundRecyclerView;

import javax.annotation.Nullable;

public class TabletImagesViewModel extends ImagesViewModel {

    private TabletImagesViewCallback imagesViewCallback;
    private EditCompoundRecyclerView editCompoundRecyclerView;
    private @Nullable ImageDetails chosenImage = null;

    public TabletImagesViewModel(ImagesCompoundRecyclerView imagesCompoundRecyclerView,
                          ImagesProvider imagesProvider,
                          TabletImagesViewCallback imagesViewCallback,
                          EditCompoundRecyclerView editCompoundRecyclerView) {
        super(imagesCompoundRecyclerView, imagesProvider, imagesViewCallback);
        this.imagesViewCallback = imagesViewCallback;
        this.editCompoundRecyclerView = editCompoundRecyclerView;
    }

    @Override
    protected void onItemViewClick(ImageDetails imageDetails) {
        chosenImage = imageDetails;
        if (!editCompoundRecyclerView.getChangedTags().isEmpty()) {
            imagesViewCallback.showRejectChangesDialog();
        } else {
            imagesViewCallback.showEditView(imageDetails);
        }
    }

    public void onRejectTagsChangesDialogPositive() {
        if (chosenImage != null) {
            imagesViewCallback.showEditView(chosenImage);
            imagesViewCallback.changeViewToDefaultMode();
        }
    }
}
