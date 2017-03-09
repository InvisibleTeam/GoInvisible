package com.invisibleteam.goinvisible.mvvm.edition.callback;

import com.invisibleteam.goinvisible.model.Tag;

import java.util.List;

public interface EditViewModelCallback {

    void onClear(Tag tag);

    void onClear(List<Tag> tagsList);

    void onEditStarted(Tag tag);

    void onEditEnded(Tag tag);

    void onEditEnded(List<Tag> tagsList);

    void onTagsUpdated();

    void onEditError();
}
