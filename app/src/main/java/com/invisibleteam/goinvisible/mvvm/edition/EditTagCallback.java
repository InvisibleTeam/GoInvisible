package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

public interface EditTagCallback {
    void openPlacePickerView(Tag tag);

    void showUnmodifiableTagMessage();

    void showTagEditionErrorMessage();

    void showTagEditionView(Tag tag);
}
