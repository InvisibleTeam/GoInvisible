package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.Tag;

interface EditTagListener {

    void openEditDialog(Tag tag, OnTagActionListener listener);
}
