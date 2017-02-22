package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

import java.util.List;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final TagsManager manager;
    private final EditTagCallback listener;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager manager,
                  EditTagCallback listener) {
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
    public void onClear(List<Tag> tagsList) {
        if (manager.clearTags(tagsList)) {
            onEditEnded(tagsList);
        }
    }

    @Override
    public void onEditStarted(Tag tag) {
        if (ExifInterface.TAG_GPS_LATITUDE.equals(tag.getKey())) {
            listener.openPlacePickerView(tag);
            return;
        }

        switch (tag.getTagType().getInputType()) {
            case UNMODIFIABLE:
                listener.showUnmodifiableTagMessage();
                break;
            case INDEFINITE:
                listener.showTagEditionErrorMessage();
                break;
            default:
                listener.showTagEditionView(tag);
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
        listener.onTagsChanged();
    }

    @Override
    public void onEditError() {
        listener.showTagEditionErrorMessage();
    }
}
