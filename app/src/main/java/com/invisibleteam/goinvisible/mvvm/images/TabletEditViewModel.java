package com.invisibleteam.goinvisible.mvvm.images;


import android.databinding.ObservableBoolean;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagCallback;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.mvvm.edition.EditTagsTabletCallback;
import com.invisibleteam.goinvisible.mvvm.edition.TagsManager;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import java.util.List;

public class TabletEditViewModel extends EditViewModel {

    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);
    private EditTagsTabletCallback editTagsTabletCallback;

    public TabletEditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, EditTagCallback callback) {
        super(editCompoundRecyclerView, callback);
    }

    public void initialize(ImageDetails imageDetails, TagsManager manager, EditTagsTabletCallback callback) {
        title.set(imageDetails.getName());
        imageUrl.set(imageDetails.getPath());
        this.manager = manager;
        this.editTagsTabletCallback = callback;
        editCompoundRecyclerView.setOnTagActionListener(this);
        editCompoundRecyclerView.prepareTagsList(manager.getAllTags());
    }

    public void onApproveChanges() {
        if (saveTags()) {
            editCompoundRecyclerView.updateTagListAfterChanges(manager.getAllTags());
            isInEditMode.set(!editCompoundRecyclerView.getChangedTags().isEmpty());
            editTagsTabletCallback.showTagsSuccessfullyUpdatedMessage();
        } else {
            editTagsTabletCallback.showTagsUpdateFailureMessage();
        }
    }

    public void onClearAllTags() {
        onClear(manager.getAllTags());
    }

    private boolean saveTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return manager.editTags(changedTags);
    }

    public ObservableBoolean getIsInEditMode() {
        return isInEditMode;
    }

    @Override
    public void onTagsUpdated() {
        isInEditMode.set(!editCompoundRecyclerView.getChangedTags().isEmpty());
        super.onTagsUpdated();
    }

    void changeViewToDefaultMode() {
        isInEditMode.set(false);
    }
}
