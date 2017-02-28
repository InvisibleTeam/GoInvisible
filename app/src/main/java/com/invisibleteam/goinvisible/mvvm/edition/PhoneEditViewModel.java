package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.PhoneTagEditionStartCallback;
import com.invisibleteam.goinvisible.util.TagsManager;

public class PhoneEditViewModel extends EditViewModel {

    private PhoneTagEditionStartCallback editTagCallback;

    PhoneEditViewModel(
            String title,
            String imageUrl,
            EditCompoundRecyclerView editCompoundRecyclerView,
            TagsManager manager,
            PhoneTagEditionStartCallback editTagCallback) {
        super(title, imageUrl, editCompoundRecyclerView, manager, editTagCallback);
        this.editTagCallback = editTagCallback;
    }

    @Override
    public void onTagsUpdated() {
        super.onTagsUpdated();
        editTagCallback.changeViewToEditMode();
    }
}
