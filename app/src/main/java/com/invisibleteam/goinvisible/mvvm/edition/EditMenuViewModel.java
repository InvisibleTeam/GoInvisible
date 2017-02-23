package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

import java.util.List;

class EditMenuViewModel {
    private final TagsManager tagsManager;
    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final OnTagActionListener onTagActionListener;
    private final EditMenuViewCallback editMenuViewCallback;

    EditMenuViewModel(
            TagsManager tagsManager,
            EditCompoundRecyclerView editCompoundRecyclerView,
            OnTagActionListener onTagActionListener,
            EditMenuViewCallback editMenuViewCallback) {
        this.tagsManager = tagsManager;
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.onTagActionListener = onTagActionListener;
        this.editMenuViewCallback = editMenuViewCallback;
    }

    void onBackClick() {
        if (areTagsChanged()) {
            editMenuViewCallback.showRejectChangesDialog();
        } else {
            editMenuViewCallback.navigateChangedImagesScreen();
        }
    }

    private boolean areTagsChanged() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return !changedTags.isEmpty();
    }

    void onRejectTagsChangesDialogPositive() {
        editMenuViewCallback.navigateBack();
    }

    void onClearAllTagsClick() {
        List<Tag> changedTags = editCompoundRecyclerView.getAllTags();
        onTagActionListener.onClear(changedTags);
    }

    void onApproveChangesClick() {
        if (saveTags()) {
            editCompoundRecyclerView.updateTagListAfterChanges(tagsManager.getAllTags());
            editMenuViewCallback.showTagsSuccessfullyUpdatedMessage();
            editMenuViewCallback.changeViewToDefaultMode();
        } else {
            editMenuViewCallback.showTagsUpdateFailureMessage();
        }
    }

    private boolean saveTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return tagsManager.editTags(changedTags);
    }

    boolean isInEditState() {
        return editCompoundRecyclerView.getChangedTags().size() > 0;
    }
}
