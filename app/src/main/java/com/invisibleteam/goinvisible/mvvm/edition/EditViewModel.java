package com.invisibleteam.goinvisible.mvvm.edition;

import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.util.binding.ObservableString;
import com.invisibleteam.goinvisible.util.TagsManager;

import java.util.List;

public class EditViewModel implements EditViewModelCallback {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");
    private final TagEditionStartCallback tagEditionStartCallback;
    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private TagsManager manager;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager manager,
                  TagEditionStartCallback callback) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.manager = manager;
        this.tagEditionStartCallback = callback;

        initRecyclerView(manager.getAllTags());
    }

    public EditViewModel(EditCompoundRecyclerView editCompoundRecyclerView, TagEditionStartCallback callback) {
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.tagEditionStartCallback = callback;
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
            tagEditionStartCallback.openPlacePickerView(tag);
            return;
        }

        switch (tag.getTagType().getInputType()) {
            case UNMODIFIABLE:
                tagEditionStartCallback.showUnmodifiableTagMessage();
                break;
            case INDEFINITE:
                tagEditionStartCallback.showTagEditionErrorMessage();
                break;
            default:
                tagEditionStartCallback.showTagEditionView(tag);
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
    }

    @Override
    public void onEditError() {
        tagEditionStartCallback.showTagEditionErrorMessage();
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    protected EditCompoundRecyclerView getEditCompoundRecyclerView() {
        return editCompoundRecyclerView;
    }

    protected TagsManager getManager() {
        return manager;
    }

    protected void setManager(TagsManager manager) {
        this.manager = manager;
    }

}
