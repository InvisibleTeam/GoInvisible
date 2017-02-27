package com.invisibleteam.goinvisible.mvvm.images;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditTagsTabletCallback;

import java.util.List;

public class TabletEditViewModel extends EditViewModel {

    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);
    private ImageDetails imageDetails;
    private EditTagsTabletCallback editTagsTabletCallback;

    public TabletEditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, EditTagCallback callback) {
        super(editCompoundRecyclerView, callback);
    }

    public void initialize(ImageDetails details, TagsManager manager, EditTagsTabletCallback callback) {
        imageDetails = details;
        getTitle().set(details.getName());
        getImageUrl().set(details.getPath());
        setManager(manager);
        getEditCompoundRecyclerView().setOnTagActionListener(this);
        getEditCompoundRecyclerView().prepareTagsList(manager.getAllTags());
        editTagsTabletCallback = callback;
    }

    public void onApproveChanges() {
        if (saveTags()) {
            getEditCompoundRecyclerView().updateTagListAfterChanges();
            isInEditMode.set(!getEditCompoundRecyclerView().getChangedTags().isEmpty());
            editTagsTabletCallback.showTagsSuccessfullyUpdatedMessage();
        } else {
            editTagsTabletCallback.showTagsUpdateFailureMessage();
        }
    }

    public void onClearAllTags() {
        onClear(getManager().getAllTags());
    }

    public void onShare() {
        editTagsTabletCallback.onShare(imageDetails);
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

    void changeViewToDefaultMode() {
        isInEditMode.set(false);
    }
}
