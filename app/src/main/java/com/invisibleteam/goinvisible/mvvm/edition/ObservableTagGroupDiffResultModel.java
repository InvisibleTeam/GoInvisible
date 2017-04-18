package com.invisibleteam.goinvisible.mvvm.edition;


import android.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

public class ObservableTagGroupDiffResultModel extends BaseObservable {
    private final List<TagDiffResultModel> resultModels = new ArrayList<>();

    public void clear() {
        resultModels.clear();
    }

    public void add(TagDiffResultModel model) {
        resultModels.add(model);
        notifyChange();
    }

    public void addAll(List<TagDiffResultModel> modelList) {
        resultModels.addAll(modelList);
        notifyChange();
    }

    public List<TagDiffResultModel> getResultModels() {
        return resultModels;
    }

}
