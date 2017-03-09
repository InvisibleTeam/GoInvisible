package com.invisibleteam.goinvisible.mvvm.images.tablet;


import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TabletEditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.io.FileNotFoundException;
import java.util.List;

public class TabletEditViewModel extends EditViewModel {

    private static final String TAG = TabletEditViewModel.class.getName();

    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);
    private ImageDetails imageDetails;
    private TabletEditTagCallback tabletEditTagCallback;
    private SharingHelper sharingHelper;

    public TabletEditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, TagEditionStartCallback callback,
                               SharingHelper sharingHelper) {
        super(editCompoundRecyclerView, callback);
        this.sharingHelper = sharingHelper;
    }

    public void initialize(ImageDetails details, TagsManager manager, TabletEditTagCallback callback) {
        imageDetails = details;
        getTitle().set(details.getName());
        getImageUrl().set(details.getPath());
        setManager(manager);
        getEditCompoundRecyclerView().setOnTagActionListener(this);
        getEditCompoundRecyclerView().prepareTagsList(manager.getAllTags());
        tabletEditTagCallback = callback;
    }

    public void onApproveChanges() {
        if (saveTags()) {
            getEditCompoundRecyclerView().updateTagListAfterChanges();
            isInEditMode.set(!getEditCompoundRecyclerView().getChangedTags().isEmpty());
            tabletEditTagCallback.showTagsSuccessfullyUpdatedMessage();
        } else {
            tabletEditTagCallback.showTagsUpdateFailureMessage();
        }
    }

    public void onClearAllTags() {
        onClear(getManager().getAllTags());
    }

    public void onShare() {
        try {
            Intent shareIntent = sharingHelper.buildShareImageIntent(imageDetails);
            tabletEditTagCallback.onShare(shareIntent);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error during sharing image", e);
        }
    }

    private boolean saveTags() {
        List<Tag> changedTags = getEditCompoundRecyclerView().getChangedTags();
        return getManager().editTags(changedTags);
    }

    public ObservableBoolean getIsInEditMode() {
        return isInEditMode;
    }

    @Override
    public void onTagsUpdated() {
        isInEditMode.set(!getEditCompoundRecyclerView().getChangedTags().isEmpty());
        super.onTagsUpdated();
    }

    public void changeViewToDefaultMode() {
        isInEditMode.set(false);
    }
}
