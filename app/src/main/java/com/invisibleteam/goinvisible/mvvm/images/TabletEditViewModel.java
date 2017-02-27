package com.invisibleteam.goinvisible.mvvm.images;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagsTabletCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import java.util.List;

public class TabletEditViewModel extends EditViewModel {

    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);

    public TabletEditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, EditTagCallback callback) {
        super(editCompoundRecyclerView, callback);
    }

    public void initialize(ImageDetails imageDetails, TagsManager manager, EditTagsTabletCallback callback) {
        getTitle().set(imageDetails.getName());
        getImageUrl().set(imageDetails.getPath());
        setManager(manager);
        setEditTagsTabletCallback(callback);
        getEditCompoundRecyclerView().setOnTagActionListener(this);
        getEditCompoundRecyclerView().prepareTagsList(manager.getAllTags());
    }

    public void onApproveChanges() {
        if (saveTags()) {
            getEditCompoundRecyclerView().updateTagListAfterChanges(getEditCompoundRecyclerView().getChangedTags());
            isInEditMode.set(!getEditCompoundRecyclerView().getChangedTags().isEmpty());
            getEditTagsTabletCallback().showTagsSuccessfullyUpdatedMessage();
        } else {
            getEditTagsTabletCallback().showTagsUpdateFailureMessage();
        }
    }

    public void onClearAllTags() {
        onClear(getManager().getAllTags());
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
}
