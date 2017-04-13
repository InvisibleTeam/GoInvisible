package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.invisibleteam.goinvisible.databinding.SectionEditItemViewBinding;

class SectionViewHolder extends RecyclerView.ViewHolder {

    private SectionEditItemViewModel sectionEditItemViewModel;

    SectionViewHolder(View itemView) {
        super(itemView);

        sectionEditItemViewModel = new SectionEditItemViewModel();
        SectionEditItemViewBinding sectionEditItemViewBinding = SectionEditItemViewBinding.bind(itemView);
        sectionEditItemViewBinding.setViewModel(sectionEditItemViewModel);
    }

    void setSectionName(String name) {
        sectionEditItemViewModel.setSectionName(name);
    }

    @VisibleForTesting
    SectionEditItemViewModel getSectionEditItemViewModel() {
        return sectionEditItemViewModel;
    }
}