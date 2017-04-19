package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;

public interface EditDialogInterface {
    void onEditError();

    void onEditEnded(Tag tag);
}
