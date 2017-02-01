package com.invisibleteam.goinvisible.mvvm.edition;


import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditCompoundRecyclerView;
import com.invisibleteam.goinvisible.util.ObservableString;

public class EditViewModel implements OnTagActionListener {

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");

    private final EditCompoundRecyclerView editCompoundRecyclerView;
    private final TagsManager tagsManager;
    private final EditTagListener editTagListener;

    EditViewModel(String title,
                  String imageUrl,
                  EditCompoundRecyclerView editCompoundRecyclerView,
                  TagsManager tagsManager,
                  EditTagListener editTagListener) {
        this.title.set(title);
        this.imageUrl.set(imageUrl);
        this.editCompoundRecyclerView = editCompoundRecyclerView;
        this.tagsManager = tagsManager;
        this.editTagListener = editTagListener;

        initRecyclerView();
    }

    private void initRecyclerView() {
        editCompoundRecyclerView.setOnTagActionListener(this);
        editCompoundRecyclerView.updateResults(tagsManager.getAllTags());
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    @Override
    public void onClear(Tag tag) {
        if (tagsManager.clearTag(tag)) {
            editCompoundRecyclerView.updateTag(tag);
        }
    }

    @Override
    public void onEdit(Tag tag) {
        editTagListener.openEditDialog(tag);
        //todo execute this when dialog will be closed
//        tagsManager.editTag(tag);
    }
}
