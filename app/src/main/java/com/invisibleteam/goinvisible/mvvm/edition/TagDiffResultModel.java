package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.v7.util.DiffUtil;

public class TagDiffResultModel {
    private final int groupPosition;
    private final DiffUtil.DiffResult diffResult;

    public TagDiffResultModel(int groupPosition, DiffUtil.DiffResult diffResult) {
        this.groupPosition = groupPosition;
        this.diffResult = diffResult;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public DiffUtil.DiffResult getDiffResult() {
        return diffResult;
    }
}
