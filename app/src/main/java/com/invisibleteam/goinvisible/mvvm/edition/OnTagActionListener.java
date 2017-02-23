package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

import java.util.List;

public interface OnTagActionListener {

    void onClear(Tag tag);

    void onClear(List<Tag> tagsList);

    void onEditStarted(Tag tag);

    void onEditEnded(Tag tag);

    void onEditEnded(List<Tag> tagsList);

    void onTagsUpdated();

    void onEditError();
}
