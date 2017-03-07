package com.invisibleteam.goinvisible.mvvm.images.tablet;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.util.TagsManager;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TabletEditTagCallback;

import java.util.List;

public class TabletEditViewModel extends EditViewModel {

    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);
    private ImageDetails imageDetails;
    private TabletEditTagCallback tabletEditTagCallback;

    public TabletEditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, TagEditionStartCallback callback) {
        super(editCompoundRecyclerView, callback);
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
        tabletEditTagCallback.onShare(imageDetails, new SharingHelper());
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
