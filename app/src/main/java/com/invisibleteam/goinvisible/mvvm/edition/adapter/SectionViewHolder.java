package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.invisibleteam.goinvisible.databinding.SectionEditItemViewBinding;

class SectionViewHolder extends ViewHolder {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    SectionEditItemViewModel sectionEditItemViewModel;

    SectionEditItemViewBinding sectionEditItemViewBinding;

    SectionViewHolder(View itemView) {
        super(itemView);

        sectionEditItemViewModel = new SectionEditItemViewModel();
        sectionEditItemViewBinding = SectionEditItemViewBinding.bind(itemView);
        sectionEditItemViewBinding.setViewModel(sectionEditItemViewModel);
    }

    void setSectionName(String name) {
        sectionEditItemViewModel.setSectionName(name);
    }
}