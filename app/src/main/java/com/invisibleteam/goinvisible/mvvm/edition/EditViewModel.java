package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

public class EditViewModel {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final TagsProvider tagsProvider;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsProvider tagsProvider) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.tagsProvider = tagsProvider;

        initRecyclerView();
    }

    private void initRecyclerView() {
        editCompoundRecyclerView.updateResults(tagsProvider.getAllTags());
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }
}
