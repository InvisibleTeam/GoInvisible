package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;
import com.invisibleteam.goinvisible.util.TextViewUtil;

import javax.annotation.Nullable;

class TagViewHolder extends ChildViewHolder {

    private EditItemViewModel editItemViewModel;
    private EditItemViewBinding editItemViewBinding;

    TagViewHolder(View itemView, @Nullable EditViewModelCallback editViewModelCallback) {
        super(itemView);

        editItemViewModel = new EditItemViewModel();
        editItemViewBinding = EditItemViewBinding.bind(itemView);
        editItemViewBinding.setViewModel(editItemViewModel);
        TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
        setupListeners(editViewModelCallback);
    }

    private void setupListeners(@Nullable EditViewModelCallback editViewModelCallback) {
        editItemViewBinding.clearButton.setOnClickListener(v -> {
            if (editViewModelCallback != null) {
                Tag tag = (Tag) itemView.getTag();
                editViewModelCallback.onClear(tag);
            }
        });

        itemView.setOnClickListener(view -> {
            if (editViewModelCallback != null) {
                Tag tag = (Tag) itemView.getTag();
                editViewModelCallback.onEditStarted(tag);
            }
        });
    }

    void setTag(Tag tag) {
        this.itemView.setTag(tag);
        this.editItemViewModel.setModel(tag);
    }

    @VisibleForTesting
    EditItemViewModel getEditItemViewModel() {
        return editItemViewModel;
    }
}