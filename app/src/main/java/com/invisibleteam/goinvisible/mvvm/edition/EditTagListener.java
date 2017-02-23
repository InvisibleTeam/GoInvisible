package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

public interface EditTagListener {

    void openTagEditionView(Tag tag);

    void onTagsChanged();

    void onEditError();
}
