package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.v7.util.DiffUtil;

import com.invisibleteam.goinvisible.model.Tag;

import java.util.List;

public class TagListDiffCallback extends DiffUtil.Callback {

    private List<Tag> oldList;
    private List<Tag> newList;

    public TagListDiffCallback(List<Tag> oldList, List<Tag> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Tag oldTag = oldList.get(oldItemPosition);
        Tag newTag = newList.get(newItemPosition);
        return oldTag.getKey().equals(newTag.getKey());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Tag oldTag = oldList.get(oldItemPosition);
        Tag newTag = newList.get(newItemPosition);
        if (oldTag.getValue() == null && newTag.getValue() == null) {
            return true;
        }
        if (oldTag.getValue() != null && oldTag.getValue().equals(newTag.getValue())) {
            return true;
        }
        return false;
    }
}
