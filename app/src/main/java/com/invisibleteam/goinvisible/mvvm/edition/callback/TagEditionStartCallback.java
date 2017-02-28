package com.invisibleteam.goinvisible.mvvm.edition.callback;

import com.invisibleteam.goinvisible.model.Tag;

public interface TagEditionStartCallback {
    void openPlacePickerView(Tag tag);

    void showUnmodifiableTagMessage();

    void showTagEditionErrorMessage();

    void showTagEditionView(Tag tag);
}
