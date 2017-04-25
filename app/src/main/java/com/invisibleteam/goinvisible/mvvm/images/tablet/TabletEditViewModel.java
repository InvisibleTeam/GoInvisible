package com.invisibleteam.goinvisible.mvvm.images.tablet;


import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.util.Log;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagDiffMicroServiceFactory;
import com.invisibleteam.goinvisible.mvvm.edition.TagListDiffMicroService;
import com.invisibleteam.goinvisible.util.LifecycleBinder;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.IOException;

import javax.annotation.Nullable;

public class TabletEditViewModel extends EditViewModel {

    private static final String TAG = TabletEditViewModel.class.getSimpleName();
    private static final String IMAGE_DETAILS_EXTRA_KEY = "image_details_extra_key";

    private ObservableBoolean inEditState = new ObservableBoolean(false);

    public TabletEditViewModel(EditViewModelCallback callback, TagDiffMicroServiceFactory microServiceFactory, TagListDiffMicroService
            listDiffMicroService, LifecycleBinder lifecycleBinder) {
        super(callback, microServiceFactory, listDiffMicroService, lifecycleBinder);
    }

    @Override
    protected void updateViewState() {
        inEditState.set(isInEditState());
    }

    public ObservableBoolean getInEditState() {
        return inEditState;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(IMAGE_DETAILS_EXTRA_KEY, imageDetails);
        super.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(@Nullable Bundle bundle, TagsManagerProvider tagsManagerProvider) {
        if (bundle != null) {
            ImageDetails imageDetails = bundle.getParcelable(IMAGE_DETAILS_EXTRA_KEY);
            if (imageDetails != null) {
                try {
                    TagsManager tagsManager = tagsManagerProvider.buildTagsManager(imageDetails.getPath());
                    initialize(imageDetails, tagsManager);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    //TODO Log crashlitycs
                }
            }
        }
        super.onRestoreInstanceState(bundle);
    }

    public interface TagsManagerProvider {
        TagsManager buildTagsManager(String imagePath) throws IOException;
    }
}
