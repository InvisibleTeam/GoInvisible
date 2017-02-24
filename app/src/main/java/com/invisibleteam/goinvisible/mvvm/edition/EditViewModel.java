package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.ImageDetails;

import android.databinding.ObservableBoolean;
import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

import java.util.List;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");
    private final ObservableBoolean isInEditMode = new ObservableBoolean(false);

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager manager;
    private EditTagCallback editTagCallback;
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

    public ObservableBoolean getIsInEditMode() {
        return isInEditMode;
    }

    public void updateRecyclerView(ImageDetails imageDetails, TagsManager manager, EditTagsTabletCallback callback) {
        title.set(imageDetails.getName());
        imageUrl.set(imageDetails.getPath());
        this.manager = manager;
        this.editTagsTabletCallback = callback;
        editCompoundRecyclerView.setOnTagActionListener(this);
        editCompoundRecyclerView.prepareTagsList(manager.getAllTags());
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

    public void onClearAllTags() {
        onClear(manager.getAllTags());
    }

    public void onApproveChanges() {
        if (saveTags()) {
            editCompoundRecyclerView.updateTagListAfterChanges(manager.getAllTags());
            isInEditMode.set(!editCompoundRecyclerView.getChangedTags().isEmpty());
            editTagsTabletCallback.showTagsSuccessfullyUpdatedMessage();
        } else {
            editTagsTabletCallback.showTagsUpdateFailureMessage();
        }
    }

    private boolean saveTags() {
        List<Tag> changedTags = editCompoundRecyclerView.getChangedTags();
        return manager.editTags(changedTags);
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
        isInEditMode.set(!editCompoundRecyclerView.getChangedTags().isEmpty());
        editTagCallback.changeViewToEditMode();
    }

    @Override
    public void onEditError() {
        editTagCallback.showTagEditionErrorMessage();
    }

    public void setTagsManager(TagsManager tagsManager) {
        this.manager = tagsManager;
    }
}
