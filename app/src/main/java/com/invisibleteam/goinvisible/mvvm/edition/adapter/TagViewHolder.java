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
        //TODO remove this constructor

        editItemViewModel = new EditItemViewModel();
        editItemViewBinding = EditItemViewBinding.bind(itemView);
        editItemViewBinding.setViewModel(editItemViewModel);
        TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
        setupListeners(editViewModelCallback);
    }


    public TagViewHolder(View itemView, @Nullable EditItemGroupAdapter.ItemActionListener itemActionListener) {
        super(itemView);

        editItemViewModel = new EditItemViewModel(itemActionListener);
        editItemViewBinding = EditItemViewBinding.bind(itemView);
        editItemViewBinding.setViewModel(editItemViewModel);
        TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
    }

    private void setupListeners(@Nullable EditViewModelCallback editViewModelCallback) {
        //TODO REMOVE this method
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
        //TODO REMOVE this method
        this.itemView.setTag(tag);
        this.editItemViewModel.setModel(tag);
    }

    void setModel(int parentPosition, int childPosition, Tag tag) {
        editItemViewModel.setModel(parentPosition, childPosition, tag);
    }

    @VisibleForTesting
    EditItemViewModel getEditItemViewModel() {
        return editItemViewModel;
    }
}