package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final TagsManager manager;
    private final EditTagListener listener;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager manager,
                  EditTagListener listener) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.manager = manager;
        this.listener = listener;

        initRecyclerView();
    }

    private void initRecyclerView() {
        editCompoundRecyclerView.setOnTagActionListener(this);
        editCompoundRecyclerView.updateResults(manager.getAllTags());
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    @Override
    public void onClear(Tag tag) {
        if (manager.clearTag(tag)) {
            onEditEnded(tag);
        }
    }

    @Override
    public void onEditStarted(Tag tag) {
        listener.openEditDialog(tag);
    }

    @Override
    public void onEditEnded(Tag tag) {
        editCompoundRecyclerView.updateTag(tag);
    }

    @Override
    public void onTagsUpdated() {
        listener.onTagsChanged();
    }
}
