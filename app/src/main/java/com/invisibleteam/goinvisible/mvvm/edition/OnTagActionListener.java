package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

public interface OnTagActionListener {

    void onClear(Tag tag);

    void onEditStarted(Tag tag);

    void onEditEnded(Tag tag);

    void onTagsUpdated();
}
