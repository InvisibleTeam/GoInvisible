package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

import java.util.List;

public class EditViewModel implements OnTagActionListener {

    protected final ObservableString title = new ObservableString("");
    protected final ObservableString imageUrl = new ObservableString("");

    protected final EditCompoundRecyclerView editCompoundRecyclerView;
    protected TagsManager manager;
    protected EditTagCallback editTagCallback;
    protected EditTagsTabletCallback editTagsTabletCallback;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager manager,
                  EditTagCallback editTagCallback) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.manager = manager;
        this.editTagCallback = editTagCallback;

        initRecyclerView(manager.getAllTags());
    }

    public EditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, EditTagCallback callback) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.editTagCallback = callback;
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
            editTagCallback.openPlacePickerView(tag);
            return;
        }

        switch (tag.getTagType().getInputType()) {
            case UNMODIFIABLE:
                editTagCallback.showUnmodifiableTagMessage();
                break;
            case INDEFINITE:
                editTagCallback.showTagEditionErrorMessage();
                break;
            default:
                editTagCallback.showTagEditionView(tag);
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
    }

    @Override
    public void onEditError() {
        editTagCallback.showTagEditionErrorMessage();
    }

}
