package com.invisibleteam.goinvisible.util.binding;


import android.databinding.BindingAdapter;

import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.mvvm.edition.ObservableTagGroupDiffResultModel;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditItemGroupAdapter;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditRecyclerView;

import java.util.List;

import javax.annotation.Nullable;

public class EditRecyclerViewBindingAdapter {
    @BindingAdapter("modelList")
    public static void bindList(EditRecyclerView compoundRecyclerView, @Nullable List<TagGroup> resultModel) {
        if (resultModel != null) {
            compoundRecyclerView.updateList(resultModel);
        }
    }

    @BindingAdapter("diffList")
    public static void bindDiffList(EditRecyclerView compoundRecyclerView,
                                    @Nullable ObservableTagGroupDiffResultModel diffResultListModel) {
        if (diffResultListModel != null) {
            compoundRecyclerView.updateData(diffResultListModel.getResultModels());
        }
    }

    @BindingAdapter("onItemAction")
    public static void bindClickListener(EditRecyclerView compoundRecyclerView, EditItemGroupAdapter.ItemActionListener listener) {
        compoundRecyclerView.setOnItemActionListener(listener);
    }
}
