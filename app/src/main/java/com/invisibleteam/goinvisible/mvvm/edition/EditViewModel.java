package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;

class EditViewModel {

    private EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsProvider tagsProvider;

    EditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, TagsProvider tagsProvider) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.tagsProvider = tagsProvider;

        initRecyclerView();
    }

    private void initRecyclerView() {
        editCompoundRecyclerView.updateResults(tagsProvider.getTags());
    }
}
