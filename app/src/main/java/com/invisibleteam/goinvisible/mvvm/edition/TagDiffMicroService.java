package com.invisibleteam.goinvisible.mvvm.edition;


import android.support.v7.util.DiffUtil;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.util.LifecycleBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class TagDiffMicroService {
    private final LifecycleBinder lifecycleBinder;
    private final EditListener editListener;

    public
    TagDiffMicroService(LifecycleBinder lifecycleBinder, EditListener editListener) {
        this.lifecycleBinder = lifecycleBinder;
        this.editListener = editListener;
    }

    public Observable<DiffUtil.DiffResult> buildObservable(TagGroup tagGroup) {
        return Observable.just(tagGroup)
                .compose(lifecycleBinder.bind())
                .subscribeOn(Schedulers.computation())
                .map(tagGroup1 -> {
                    ArrayList<Tag> copy = new ArrayList<>(tagGroup1.getChildList());
                    Collections.copy(copy, tagGroup1.getChildList());

                    editListener.onEditTagList(tagGroup1.getChildList());
                    return DiffUtil.calculateDiff(new TagListDiffCallback(tagGroup1.getChildList(), copy), false);
                });
    }

    public interface EditListener {
        void onEditTagList(List<Tag> tagList);
    }
}
