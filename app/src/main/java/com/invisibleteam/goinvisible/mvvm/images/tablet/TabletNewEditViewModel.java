package com.invisibleteam.goinvisible.mvvm.images.tablet;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.mvvm.edition.NewEditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagDiffMicroServiceFactory;
import com.invisibleteam.goinvisible.mvvm.edition.TagListDiffMicroService;
import com.invisibleteam.goinvisible.util.LifecycleBinder;

public class TabletNewEditViewModel extends NewEditViewModel {

    private ObservableBoolean inEditState = new ObservableBoolean(false);

    public TabletNewEditViewModel(EditViewModelCallback callback, TagDiffMicroServiceFactory microServiceFactory, TagListDiffMicroService
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
}
