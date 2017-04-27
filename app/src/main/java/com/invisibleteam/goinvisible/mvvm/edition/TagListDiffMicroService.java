package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.v7.util.DiffUtil;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.util.LifecycleBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import rx.Observable;
import rx.schedulers.Schedulers;

public class TagListDiffMicroService {
    private final LifecycleBinder lifecycleBinder;
    @Nullable
    private EditListener editListener;

    public TagListDiffMicroService(
            LifecycleBinder lifecycleBinder) {
        this.lifecycleBinder = lifecycleBinder;
    }

    public Observable<List<TagDiffResultModel>> buildObservable(List<TagGroup> tagGroupList) {
        return Observable.just(tagGroupList)
                .compose(lifecycleBinder.bind())
                .subscribeOn(Schedulers.computation())
                .map(tagGroups -> {
                    List<TagDiffResultModel> resultModels = new ArrayList<>(tagGroups.size());
                    for (int i = 0; i < tagGroups.size(); i++) {
                        TagGroup tagGroup = tagGroups.get(i);
                        ArrayList<Tag> copy = new ArrayList<>(tagGroup.getChildList());
                        Collections.copy(copy, tagGroup.getChildList());

                        if (editListener != null) {
                            for (int j = 0; j < tagGroup.getChildList().size(); j++) {
                                editListener.onEditTagList(tagGroup.getChildList(), i, j);
                            }
                        }

                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TagListDiffCallback(tagGroup.getChildList(), copy),
                                false);
                        resultModels.add(new TagDiffResultModel(i, diffResult));
                    }
                    return resultModels;
                });
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    public interface EditListener {
        void onEditTagList(List<Tag> tagList, int groupPosition, int childPosition);
    }

}
