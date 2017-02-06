package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

public interface OnTagActionListener {

    void onClear(Tag tag);

    void onEdit(Tag tag);

    void onUpdate(Tag tag);
}
