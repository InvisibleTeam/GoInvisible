package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

interface EditTagListener {

    void openTagEditionView(Tag tag);

    void onTagsChanged();

    void onEditError();
}
