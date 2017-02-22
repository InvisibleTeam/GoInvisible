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
            editMenuViewCallback.showApproveChangeTagsDialog();
        }
    }

    private boolean areTagsChanged() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return !changedTags.isEmpty();
    }

    void onApproveChangeTagsDialogPositive() {
        editMenuViewCallback.navigateBack();
    }

    void onClearAllTagsClick() {
        List<Tag> changedTags = editCompoundRecyclerView.getAllTags();
        onTagActionListener.onClear(changedTags);
    }

    void onApproveChangesClick() {
        saveTags();
        editMenuViewCallback.navigateChangedImagesScreen();
    }

    private void saveTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        tagsManager.editTags(changedTags);
    }

    boolean isInEditState() {
        return editCompoundRecyclerView.getChangedTags().size() > 0;
    }
}
