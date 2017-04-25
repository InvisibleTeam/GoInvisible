package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.util.LifecycleBinder;
import com.invisibleteam.goinvisible.util.TagsManager;

public class PhoneEditViewModel extends EditViewModel {

    private PhoneViewModelCallback callback;

    public PhoneEditViewModel(ImageDetails imageDetails, TagsManager tagsManager, PhoneViewModelCallback callback,
                              TagDiffMicroServiceFactory microServiceFactory, TagListDiffMicroService listDiffMicroService,
                              LifecycleBinder lifecycleBinder) {
        super(callback, microServiceFactory, listDiffMicroService, lifecycleBinder);
        this.callback = callback;
        initialize(imageDetails, tagsManager);
    }

    @Override
    protected void updateViewState() {
        callback.updateViewState();
    }

    public void onFinishScene() {
        if (isInEditState()) {
            callback.showRejectChangesDialog();
        } else {
            callback.finishScene();
        }
    }

    public interface PhoneViewModelCallback extends EditViewModelCallback {
        void updateViewState();

        void finishScene();
    }
}
