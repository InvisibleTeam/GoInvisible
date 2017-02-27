package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

import java.util.List;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");
    private final EditTagCallback editTagCallback;
    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager manager;
    private EditTagsTabletCallback editTagsTabletCallback;

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
        if (editCompoundRecyclerView.updateResults(tags)) {
            onTagsUpdated();
        }
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
        if (editCompoundRecyclerView.updateTag(tag)) {
            onTagsUpdated();
        }
    }

    @Override
    public void onEditEnded(List<Tag> tagsList) {
        if (editCompoundRecyclerView.updateResults(tagsList)) {
            onTagsUpdated();
        }
    }

    @Override
    public void onTagsUpdated() {
        editTagCallback.changeViewToEditMode();
    }

    @Override
    public void onEditError() {
        editTagCallback.showTagEditionErrorMessage();
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    public EditCompoundRecyclerView getEditCompoundRecyclerView() {
        return editCompoundRecyclerView;
    }

    public TagsManager getManager() {
        return manager;
    }

    public void setManager(TagsManager manager) {
        this.manager = manager;
    }

    public EditTagsTabletCallback getEditTagsTabletCallback() {
        return editTagsTabletCallback;
    }

    public void setEditTagsTabletCallback(EditTagsTabletCallback editTagsTabletCallback) {
        this.editTagsTabletCallback = editTagsTabletCallback;
    }

}
