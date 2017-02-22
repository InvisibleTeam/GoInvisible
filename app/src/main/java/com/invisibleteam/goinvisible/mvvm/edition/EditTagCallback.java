package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

interface EditTagCallback {
    void onTagsChanged();

    void openPlacePickerView(Tag tag);

    void showUnmodifiableTagMessage();

    void showTagEditionErrorMessage();

    void showTagEditionView(Tag tag);
}
