package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.invisibleteam.goinvisible.databinding.SectionEditItemViewBinding;

class SectionViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0f;
    private static final float ROTATED_POSITION = 180f;
    private final SectionEditItemViewBinding sectionEditItemViewBinding;

    private SectionEditItemViewModel sectionEditItemViewModel;

    SectionViewHolder(View itemView) {
        super(itemView);

        sectionEditItemViewModel = new SectionEditItemViewModel();
        sectionEditItemViewBinding = SectionEditItemViewBinding.bind(itemView);
        sectionEditItemViewBinding.setViewModel(sectionEditItemViewModel);
        sectionEditItemViewBinding.expandButton.setOnClickListener(v -> {
            if (isExpanded()) {
                collapseView();
            } else {
                expandView();
            }
        });
    }

    void setSectionName(String name) {
        sectionEditItemViewModel.setSectionName(name);
    }

    @VisibleForTesting
    SectionEditItemViewModel getSectionEditItemViewModel() {
        return sectionEditItemViewModel;
    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded) {
            sectionEditItemViewBinding.expandButton.setRotation(ROTATED_POSITION);
        } else {
            sectionEditItemViewBinding.expandButton.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        sectionEditItemViewBinding.expandButton.startAnimation(rotateAnimation);
    }
}