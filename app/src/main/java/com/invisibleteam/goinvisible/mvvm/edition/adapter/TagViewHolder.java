package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.util.TextViewUtil;

import javax.annotation.Nullable;

class TagViewHolder extends ChildViewHolder {

    private EditItemViewModel editItemViewModel;
    private EditItemViewBinding editItemViewBinding;


    public TagViewHolder(View itemView, @Nullable EditItemGroupAdapter.ItemActionListener itemActionListener) {
        super(itemView);

        editItemViewModel = new EditItemViewModel(itemActionListener);
        editItemViewBinding = EditItemViewBinding.bind(itemView);
        editItemViewBinding.setViewModel(editItemViewModel);
        TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
    }

    void setModel(int parentPosition, int childPosition, Tag tag) {
        editItemViewModel.setModel(parentPosition, childPosition, tag);
    }

    @VisibleForTesting
    EditItemViewModel getEditItemViewModel() {
        return editItemViewModel;
    }
}