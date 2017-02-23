package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.media.ExifInterface;

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
    private EditTagCallback callback;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager manager,
                  EditTagCallback callback) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.manager = manager;
        this.callback = callback;

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
        manager.clearTag(tag);
        onEditEnded(tag);
    }

    @Override
    public void onClear(List<Tag> tagsList) {
        manager.clearTags(tagsList);
        onEditEnded(tagsList);
    }

    @Override
    public void onEditStarted(Tag tag) {
        if (ExifInterface.TAG_GPS_LATITUDE.equals(tag.getKey())) {
            callback.openPlacePickerView(tag);
            return;
        }

        switch (tag.getTagType().getInputType()) {
            case UNMODIFIABLE:
                callback.showUnmodifiableTagMessage();
                break;
            case INDEFINITE:
                callback.showTagEditionErrorMessage();
                break;
            default:
                callback.showTagEditionView(tag);
        }
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
        callback.changeViewToEditMode();
    }

    @Override
    public void onEditError() {
        callback.showTagEditionErrorMessage();
    }

    public void setTagsManager(TagsManager tagsManager) {
        this.manager = tagsManager;
    }
}
