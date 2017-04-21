package com.invisibleteam.goinvisible.mvvm.edition;


import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.media.ExifInterface;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.mvvm.common.TagsUpdateStatusCallback;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditItemGroupAdapter;
import com.invisibleteam.goinvisible.mvvm.edition.callback.RejectEditionChangesCallback;
import com.invisibleteam.goinvisible.mvvm.edition.callback.TagEditionStartCallback;
import com.invisibleteam.goinvisible.util.LifecycleBinder;
import com.invisibleteam.goinvisible.util.TagsManager;
import com.invisibleteam.goinvisible.util.binding.ObservableString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NewEditViewModel implements EditItemGroupAdapter.ItemActionListener, EditDialogInterface {

    private static final String MODEL_LIST_EXTRA_KEY = "model_list_extra";
    private static final String DIFF_MAP_EXTRA_KEY = "diff_map_extra";

    private final ObservableString title = new ObservableString("");
    private final ObservableString imageUrl = new ObservableString("");
    private final ObservableArrayList<TagGroup> modelList = new ObservableArrayList<>();
    private final ObservableTagGroupDiffResultModel diffList = new ObservableTagGroupDiffResultModel();

    private final TagsManager tagsManager;
    private final TagDiffMicroServiceFactory microServiceFactory;
    private final TagListDiffMicroService listDiffMicroService;
    private final LifecycleBinder lifecycleBinder;
    protected final EditViewModelCallback callback;

    private final HashMap<String, Tag> diffMap = new HashMap<>();
    private List<TagGroup> originalModelList;
    private int editedGroupPosition = 0;
    private int editedChildPosition = 0;

    public NewEditViewModel(
            ImageDetails imageDetails,
            TagsManager tagsManager,
            EditViewModelCallback callback,
            TagDiffMicroServiceFactory microServiceFactory,
            TagListDiffMicroService listDiffMicroService,
            LifecycleBinder lifecycleBinder) {
        this.tagsManager = tagsManager;
        this.callback = callback;
        this.microServiceFactory = microServiceFactory;
        this.listDiffMicroService = listDiffMicroService;
        this.lifecycleBinder = lifecycleBinder;
        title.set(imageDetails.getName());
        imageUrl.set(imageDetails.getPath());

        List<Tag> allTags = tagsManager.getAllTags();
        originalModelList = TagGroupUtil.createTagGroups(allTags);
        List<TagGroup> tagGroups = TagGroupUtil.createTagGroups(TagGroupUtil.deepCopyList(allTags));
        modelList.addAll(tagGroups);

        setupListDiffMicroService();
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getImageUrl() {
        return imageUrl;
    }

    public ObservableArrayList<TagGroup> getModelList() {
        return modelList;
    }

    public ObservableTagGroupDiffResultModel getDiffList() {
        return diffList;
    }

    public EditItemGroupAdapter.ItemActionListener getItemActionListener() {
        return this;
    }

    @Override
    public void onItemClick(int parentPosition, int childPosition, Tag tag) {
        this.editedGroupPosition = parentPosition;
        this.editedChildPosition = childPosition;

        if (ExifInterface.TAG_GPS_LATITUDE.equals(tag.getKey())) {
            callback.openPlacePickerView(tag.copy());
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
                callback.showTagEditionView(tag.copy());
        }
    }

    @Override
    public void onItemClearClick(int parentPosition, int childPosition, Tag tag) {
        TagGroup tagGroup = modelList.get(parentPosition);
        Tag tagCopy = tag.copy();

        editTag(
                parentPosition,
                childPosition,
                tagCopy,
                tagList -> {
                    tagsManager.clearTag(tagCopy);
                    tagGroup.getChildList().set(childPosition, tagCopy);
                });
    }

    private void setupListDiffMicroService() {
        listDiffMicroService.setEditListener((tagList, groupPosition, childPosition) -> {
            Tag tagCopy = tagList.get(childPosition).copy();
            tagList.set(childPosition, tagCopy);
            tagsManager.clearTag(tagCopy);
            checkDifferentFromOriginal(groupPosition, childPosition, tagCopy);
        });
    }

    void onClearAllClick() {
        listDiffMicroService
                .buildObservable(modelList)
                .map(tagDiffResultModels -> {
                    diffList.clear();
                    diffList.addAll(tagDiffResultModels);
                    return tagDiffResultModels;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tagDiffResultModels -> callback.updateViewState());
    }

    @Override
    public void onEditError() {
        callback.showTagEditionErrorMessage();
    }

    @Override
    public void onEditEnded(Tag tag) {
        editTag(
                editedGroupPosition,
                editedChildPosition,
                tag,
                tagList -> tagList.set(editedChildPosition, tag));
    }

    void onApproveChangesClick() {
        Observable.just(diffMap)
                .compose(lifecycleBinder.bind())
                .subscribeOn(Schedulers.computation())
                .map((Func1<Map<String, Tag>, List<Tag>>) stringTagMap -> new ArrayList<>(diffMap.values()))
                .map(tags -> {
                    if (tagsManager.saveTags(tags)) {
                        return tags;
                    }
                    throw Exceptions.propagate(new Exception("There were problems saving tags."));
                })
                .map(tagList -> {
                    TagGroupUtil.updateTagList(originalModelList, tagsManager.getAllTags());
                    diffMap.clear();
                    return originalModelList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TagGroup>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.showTagsUpdateFailureMessage();
                    }

                    @Override
                    public void onNext(List<TagGroup> tags) {
                        callback.updateViewState();
                        callback.showTagsSuccessfullyUpdatedMessage();
                    }
                });
    }

    void onShareClick() {
        if (isInEditState()) {
            callback.showViewInEditStateInformation();
        } else {
            callback.shareImage();
        }
    }

    boolean isInEditState() {
        return !diffMap.isEmpty();
    }

    private void editTag(int parentPosition,
                         int childPosition,
                         Tag tag,
                         TagDiffMicroService.EditListener editListener) {
        TagGroup tagGroup = modelList.get(parentPosition);

        microServiceFactory
                .buildService(editListener)
                .buildObservable(tagGroup)
                .map(diffResult -> {
                    diffList.clear();
                    diffList.add(new TagDiffResultModel(parentPosition, diffResult));
                    checkDifferentFromOriginal(parentPosition, childPosition, tag);
                    return diffResult;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> callback.updateViewState());
    }

    private void checkDifferentFromOriginal(int editedGroupPosition, int editedChildPosition, Tag tag) {
        Tag originalTag = originalModelList.get(editedGroupPosition).getChildList().get(editedChildPosition);
        if (originalTag.getFormattedValue().equals(tag.getFormattedValue())) {
            diffMap.remove(tag.getKey());
            return;
        }
        diffMap.put(tag.getKey(), tag);
    }

    void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(MODEL_LIST_EXTRA_KEY, modelList);
        bundle.putSerializable(DIFF_MAP_EXTRA_KEY, diffMap);

    }

    void onRestoreInstanceState(@Nullable Bundle bundle) {
        if (bundle == null) {
            return;
        }

        if (bundle.containsKey(MODEL_LIST_EXTRA_KEY)) {
            List<TagGroup> savedModelList = bundle.getParcelableArrayList(MODEL_LIST_EXTRA_KEY);
            if (savedModelList != null) {
                TagGroupUtil.updateTagGroupList(modelList, savedModelList);
            }
        }
        if (bundle.containsKey(DIFF_MAP_EXTRA_KEY)) {
            Serializable serializable = bundle.getSerializable(DIFF_MAP_EXTRA_KEY);
            if (serializable != null) {
                Map<String, Tag> map = (Map<String, Tag>) serializable;
                diffMap.putAll(map);
            }
        }
    }

    public void onFinishScene() {
        if (isInEditState()) {
            callback.showRejectChangesDialog();
        } else {
            callback.finishScene();
        }
    }

    public interface EditViewModelCallback extends TagEditionStartCallback, TagsUpdateStatusCallback, RejectEditionChangesCallback {
        void updateViewState();

        void shareImage();

        void showViewInEditStateInformation();

        void finishScene();
    }
}
