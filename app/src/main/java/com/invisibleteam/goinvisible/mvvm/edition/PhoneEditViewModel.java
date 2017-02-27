package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.PhoneEditTagCallback;

public class PhoneEditViewModel extends EditViewModel {

    private PhoneEditTagCallback editTagCallback;

    PhoneEditViewModel(
            String title,
            String imageUrl,
            EditCompoundRecyclerView editCompoundRecyclerView,
            TagsManager manager,
            PhoneEditTagCallback editTagCallback) {
        super(title, imageUrl, editCompoundRecyclerView, manager, editTagCallback);
        this.editTagCallback = editTagCallback;
    }

    @Override
    public void onTagsUpdated() {
        super.onTagsUpdated();
        editTagCallback.changeViewToEditMode();
    }
}
