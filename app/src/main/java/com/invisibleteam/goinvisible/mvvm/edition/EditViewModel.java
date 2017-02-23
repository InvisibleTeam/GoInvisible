package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

import java.util.ArrayList;
import java.util.List;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager manager;
    private EditTagListener listener;

    public EditViewModel(String title,
                         String imageUrl,
                         EditCompoundRecyclerView editCompoundRecyclerView,
                         TagsManager manager,
                         EditTagListener listener) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.manager = manager;
        this.listener = listener;

        initRecyclerView(manager.getAllTags());
    }

    public EditViewModel(EditCompoundRecyclerView editCompoundRecyclerView) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;

        initRecyclerView(new ArrayList<>());
    }

    private void initRecyclerView(List<Tag> tags) {
        editCompoundRecyclerView.setOnTagActionListener(this);
        editCompoundRecyclerView.updateResults(tags);
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
    public void onClear(List<Tag> tagsList) {
        if (manager.clearTags(tagsList)) {
            onEditEnded(tagsList);
        }
    }

    @Override
    public void onEditStarted(Tag tag) {
        listener.openTagEditionView(tag);
    }

    @Override
    public void onEditEnded(Tag tag) {
        editCompoundRecyclerView.updateTag(tag);
    }

    @Override
    public void onEditEnded(List<Tag> tagsList) {
        editCompoundRecyclerView.updateResults(tagsList);
    }

    @Override
    public void onTagsUpdated() {
        listener.onTagsChanged();
    }

    @Override
    public void onEditError() {
        listener.onEditError();
    }

    public void setTagsManager(TagsManager tagsManager) {
        this.manager = tagsManager;
    }
}
