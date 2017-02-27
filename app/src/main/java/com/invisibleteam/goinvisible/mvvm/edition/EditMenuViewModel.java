package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditMenuViewCallback;

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
        if (isInEditState()) {
            editMenuViewCallback.showRejectChangesDialog();
        } else {
            editMenuViewCallback.navigateChangedImagesScreen();
        }
    }

    void onRejectTagsChangesDialogPositive() {
        editMenuViewCallback.navigateBack();
    }

    void onClearAllTagsClick() {
        onTagActionListener.onClear(tagsManager.getAllTags());
    }

    void onApproveChangesClick() {
        if (saveTags()) {
            editCompoundRecyclerView.updateTagListAfterChanges();
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
