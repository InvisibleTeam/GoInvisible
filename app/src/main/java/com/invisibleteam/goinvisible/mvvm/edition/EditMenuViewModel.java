package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditTagsUpdateStatusViewCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.util.List;

class EditMenuViewModel {
    private final TagsManager tagsManager;
    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final EditViewModelCallback editViewModelCallback;
    private final EditTagsUpdateStatusViewCallback editMenuViewCallback;

    EditMenuViewModel(
            TagsManager tagsManager,
            EditCompoundRecyclerView editCompoundRecyclerView,
            EditViewModelCallback viewModelCallback,
            EditTagsUpdateStatusViewCallback updateStatusCallback) {
        this.tagsManager = tagsManager;
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.editViewModelCallback = viewModelCallback;
        this.editMenuViewCallback = updateStatusCallback;
    }

    void onBackClick() {
        if (isInEditState()) {
            editMenuViewCallback.showRejectChangesDialog();
        } else {
            editMenuViewCallback.navigateBack();
        }
    }

    void onRejectTagsChangesDialogPositive() {
        editMenuViewCallback.navigateBack();
    }

    void onClearAllTagsClick() {
        editViewModelCallback.onClear(tagsManager.getAllTags());
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

    void onShareClick() {
        if (isInEditState()) {
            editMenuViewCallback.showViewInEditStateInformation();
        } else {
            editMenuViewCallback.shareImage();
        }
    }
}
