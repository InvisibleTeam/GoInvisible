package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.invisibleteam.goinvisible.databinding.EditItemViewBinding;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.TextViewUtil;

class TagViewHolder extends ViewHolder {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    EditItemViewModel editItemViewModel;

    EditItemViewBinding editItemViewBinding;

    TagViewHolder(View itemView, OnTagActionListener onTagActionListener) {
        super(itemView);

        editItemViewModel = new EditItemViewModel();
        editItemViewBinding = EditItemViewBinding.bind(itemView);
        editItemViewBinding.setViewModel(editItemViewModel);
        TextViewUtil.setEllipsizedForView(editItemViewBinding.tagKey);
        setupListeners(onTagActionListener);
    }

    private void setupListeners(OnTagActionListener onTagActionListener) {
        editItemViewBinding.clearButton.setOnClickListener(v -> {
            if (onTagActionListener != null) {
                Tag tag = (Tag) itemView.getTag();
                onTagActionListener.onClear(tag);
            }
        });

        itemView.setOnClickListener(view -> {
            if (onTagActionListener != null) {
                Tag tag = (Tag) itemView.getTag();
                onTagActionListener.onEditStarted(tag);
            }
        });
    }
}